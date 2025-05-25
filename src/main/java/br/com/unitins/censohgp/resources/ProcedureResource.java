package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.exceptions.BusinessException;
import br.com.unitins.censohgp.models.ProcedureModel;
import br.com.unitins.censohgp.repositories.impl.ProcedureRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/apicensohgp")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class ProcedureResource {

    @Inject
    ProcedureRepository procedureRepository;

    @GET
    @Path("/procedimentos")
    public Response findAll() {
        List<ProcedureModel> list = procedureRepository.findAll().list();
        return Response.ok(list).build();
    }

    @GET
    @Path("/procedimentos/ativos")
    public Response findAllActive() {
        List<ProcedureModel> list = procedureRepository.findAllActive();
        return Response.ok(list).build();
    }

    @GET
    @Path("/procedimentos/inativos")
    public Response findAllInactive() {
        List<ProcedureModel> list = procedureRepository.findAllInactive();
        return Response.ok(list).build();
    }

    @GET
    @Path("/procedimento/{idProcedimento}")
    public Response findById(@PathParam("idProcedimento") long id) {
        return procedureRepository.findByIdOptional(id)
                .map(procedure -> Response.ok(procedure).build())
                .orElseThrow(() -> new NotFoundException("Procedure not found"));
    }

    @POST
    @Path("/procedimento")
    @Transactional
    public Response createProcedure(@Valid ProcedureModel procedure) {
        Optional<ProcedureModel> existingProcedure = procedureRepository.findByName(procedure.getName());
        if (existingProcedure.isPresent()) {
            throw new BusinessException("Este procedimento já existe no sistema!");
        }

        procedure.setActive(true);
        procedureRepository.persist(procedure);
        return Response.status(Response.Status.CREATED).entity(procedure).build();
    }

    @PUT
    @Path("/procedimento")
    @Transactional
    public Response updateProcedure(@Valid ProcedureModel procedure) {
        if (procedureRepository.findById(procedure.getId()) == null) {
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
    @Path("/procedimento/mudar-status")
    @Transactional
    public Response updateProcedureStatus(@Valid ProcedureModel procedure) {
        Optional<ProcedureModel> existingProcedure = procedureRepository.findByIdOptional(procedure.getId());

        if (existingProcedure.isPresent()) {
            ProcedureModel procedureToUpdate = existingProcedure.get();
            try {
                procedureToUpdate.setActive(!procedureToUpdate.isActive());
                return Response.ok(procedureToUpdate).build();
            } catch (Exception e) {
                throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
            }
        } else {
            throw new BadRequestException("Procedimento informado não existe!");
        }
    }
}
