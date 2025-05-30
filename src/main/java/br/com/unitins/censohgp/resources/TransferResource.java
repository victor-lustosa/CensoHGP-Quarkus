package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.models.*;
import br.com.unitins.censohgp.models.dtos.TransferDTO;
import br.com.unitins.censohgp.repositories.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Transferências", description = "Endpoints para transferências de pacientes")
public class TransferResource {

    private static final Logger LOG = Logger.getLogger(TransferResource.class);

    @Inject
    TransferRepository transferRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PatientRepository patientRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @GET
    public Response findAll() {
        List<TransferModel> transfers = transferRepository.listAll();
        return Response.ok(transfers).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") long id) {
        TransferModel transfer = transferRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Transferência não encontrada"));
        return Response.ok(transfer).build();
    }

    @GET
    @Path("/patient/{id}")
    public Response findByPatient(@PathParam("id") long patientId) {
        List<TransferModel> transfers = transferRepository.findByPatientId(patientId);
        return Response.ok(transfers).build();
    }

    @POST
    @Transactional
    public Response createTransfer(@Valid TransferDTO transferDTO) {
        LOG.infof("Creating transfer: %s", transferDTO);

        PatientModel patient = patientRepository.findByIdOptional(transferDTO.patientId())
                .orElseThrow(() -> new BadRequestException("Paciente informado não existe!"));

        UserModel user = userRepository.findByRegistration(transferDTO.registration())
                .orElseThrow(() -> new BadRequestException("Usuário informado não existe!"));

        DepartmentModel origin = departmentRepository.findByIdOptional(patient.getDepartment().getId())
                .orElseThrow(() -> new BadRequestException("Departamento de origem não foi encontrado!"));

        DepartmentModel destination = departmentRepository.findByIdOptional(transferDTO.destinationDepartmentId())
                .orElseThrow(() -> new BadRequestException("Departamento de destino não foi encontrado!"));

        TransferModel transfer = new TransferModel();
        transfer.setPatient(patient);
        transfer.setUser(user);
        transfer.setObservation(transferDTO.observation());
        transfer.setOriginDepartment(origin.getName());
        transfer.setDestinationDepartment(destination.getName());

        patient.setDepartment(destination);

        try {
            patientRepository.persist(patient);  // persist patient changes
            transferRepository.persist(transfer); // persist transfer
            return Response.status(Response.Status.CREATED).entity(transfer).build();
        } catch (Exception e) {
            LOG.error("Error saving transfer", e);
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
        }
    }
}
