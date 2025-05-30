package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.models.*;
import br.com.unitins.censohgp.models.dtos.ChecklistDTO;
import br.com.unitins.censohgp.exceptions.BusinessException;
import br.com.unitins.censohgp.repositories.*;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Path("/checklists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Checklists", description = "Endpoints para checklists de pacientes")
@RequestScoped
public class ChecklistResource {

    @Inject
    ChecklistRepository checklistRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PatientRepository patientRepository;

    @Inject
    RiskFactorRepository riskFactorRepository;

    @Inject
    ProcedureRepository procedureRepository;

    @Inject
    IncidentRepository incidentRepository;

    @GET
    public Response findAll() {
        return Response.ok(checklistRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return checklistRepository.findByIdOptional(id)
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException("Checklist não foi encontrado."))
                .build();
    }

    @GET
    @Path("/patient/{id}")
    public Response findByPatient(@PathParam("id") Long id) {
        List<ChecklistModel> checklists = checklistRepository.findByPatientId(id);
        if (checklists.isEmpty()) {
            throw new NotFoundException("Não foi encontrado checklist para o paciente.");
        }
        return Response.ok(checklists).build();
    }

    @POST
    @Transactional
    public Response save(ChecklistDTO dto) {
        if (dto.userRegistration() == null) {
            throw new BusinessException("Checklist precisa estar associado a um usuário.");
        }

        ChecklistModel checklist = new ChecklistModel();

        UserModel user = userRepository.findByRegistration(dto.userRegistration())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        PatientModel patient = patientRepository.findByIdOptional(dto.patientId())
                .orElseThrow(() -> new NotFoundException("Paciente não encontrado."));

        checklist.setUser(user);
        checklist.setPatient(patient);
        checklist.setObservation(dto.observation());

        if (dto.riskFactors() != null) {
            checklist.setRiskFactors(dto.riskFactors().stream()
                    .map(id -> riskFactorRepository.findByIdOptional(id)
                            .orElseThrow(() -> new NotFoundException("Fator de Risco não encontrado.")))
                    .collect(Collectors.toList()));
        }

        if (dto.incidents() != null) {
            checklist.setIncidents(dto.incidents().stream()
                    .map(id -> incidentRepository.findByIdOptional(id)
                            .orElseThrow(() -> new NotFoundException("Incidente não encontrado.")))
                    .collect(Collectors.toList()));
        }

        if (dto.procedures() != null) {
            checklist.setProcedures(dto.procedures().stream()
                    .map(id -> procedureRepository.findByIdOptional(id)
                            .orElseThrow(() -> new NotFoundException("Procedimento não encontrado.")))
                    .collect(Collectors.toList()));
        }

        checklistRepository.persist(checklist);
        return Response.status(Response.Status.CREATED).entity(checklist).build();
    }
}
