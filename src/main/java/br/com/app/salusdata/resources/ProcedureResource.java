package br.com.app.salusdata.resources;

import br.com.app.salusdata.exceptions.BusinessException;
import br.com.app.salusdata.models.ProcedureModel;
import br.com.app.salusdata.repositories.ProcedureRepository;
import io.quarkus.security.PermissionsAllowed;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/procedures")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Procedimentos", description = "Endpoints para procedimentos")
public class ProcedureResource {

    @Inject
    ProcedureRepository procedureRepository;

    @GET
    public List<ProcedureModel> findAll() {
        return procedureRepository.findAllByName();
    }

    @GET
    @Path("/actives")
    public List<ProcedureModel> findAllActive() {
        return procedureRepository.findAllActive();
    }

    @GET
    @Path("/inactives")
    public List<ProcedureModel> findAllInactive() {
        return procedureRepository.findAllInactive();
    }

    @GET
    @Path("/{id}")
    public ProcedureModel findById(@PathParam("id") long id) {
        return procedureRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Procedimento não encontrado."));
    }

    @POST
    @Transactional
    @PermissionsAllowed("ROLE_ADMIN")
    public Response createProcedure(@Valid ProcedureModel procedure) {
        if (procedureRepository.findByName(procedure.getName()).isPresent()) {
            throw new BusinessException("Este procedimento já existe no sistema!");
        }
        procedure.setActive(true);
        procedureRepository.persist(procedure);
        return Response.status(Response.Status.CREATED).entity(procedure).build();
    }

    @PUT
    @Transactional
    @PermissionsAllowed("ROLE_ADMIN")
    public Response updateProcedure(@Valid ProcedureModel procedure) {
        if (procedureRepository.findByIdOptional(procedure.getId()).isEmpty()) {
            throw new BadRequestException("Procedimento informado não existe!");
        }

        try {
            procedureRepository.getEntityManager().merge(procedure);
            return Response.ok(procedure).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
        }
    }

    @PUT
    @Path("/toggle-status")
    @Transactional
    @PermissionsAllowed("ROLE_ADMIN")
    public Response updateProcedureStatus(@Valid ProcedureModel procedure) {
        var existingProcedure = procedureRepository.findByIdOptional(procedure.getId());

        if (existingProcedure.isEmpty()) {
            throw new BadRequestException("Procedimento informado não existe!");
        }

        try {
            ProcedureModel proc = existingProcedure.get();
            proc.setActive(!proc.isActive());
            procedureRepository.getEntityManager().merge(proc);
            return Response.ok(proc).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
        }
    }
}
