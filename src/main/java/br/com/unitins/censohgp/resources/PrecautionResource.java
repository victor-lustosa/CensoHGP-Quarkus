package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.exceptions.BusinessException;
import br.com.unitins.censohgp.models.PrecautionModel;
import br.com.unitins.censohgp.repositories.PrecautionRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/precautions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Precauções", description = "Endpoints para precauções")
public class PrecautionResource {

    @Inject
    PrecautionRepository precautionRepository;

    @GET
    public List<PrecautionModel> getAll() {
        return precautionRepository.findAllOrderedByName();
    }

    @GET
    @Path("/actives")
    public List<PrecautionModel> getAllActive() {
        return precautionRepository.findAllActive();
    }

    @GET
    @Path("/inactives")
    public List<PrecautionModel> getAllInactive() {
        return precautionRepository.findAllInactive();
    }

    @GET
    @Path("/{id}")
    public PrecautionModel getById(@PathParam("id") Long id) {
        return precautionRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Precaução não encontrada."));
    }

    @POST
    @Transactional
    public Response create(@Valid PrecautionModel precaution) {
        if (precautionRepository.findByName(precaution.getName()).isPresent()) {
            throw new BusinessException("Precaução já existe no sistema.");
        }
        precaution.setActive(true);
        precautionRepository.persist(precaution);
        return Response.status(Response.Status.CREATED).entity(precaution).build();
    }

    @PUT
    @Transactional
    public Response update(@Valid PrecautionModel precaution) {
        PrecautionModel existing = precautionRepository.findByIdOptional(precaution.getId())
                .orElseThrow(() -> new BadRequestException("Precaução não encontrada."));
        precaution.setId(existing.getId()); // Keep consistent ID
        precautionRepository.getEntityManager().merge(precaution);
        return Response.status(Response.Status.CREATED).entity(precaution).build();
    }

    @PUT
    @Transactional
    @Path("/toggle-status")
    public Response toggleStatus(@Valid PrecautionModel precaution) {
        PrecautionModel existing = precautionRepository.findByIdOptional(precaution.getId())
                .orElseThrow(() -> new BadRequestException("Precaução não encontrada."));
        existing.setActive(!existing.isActive());
        precautionRepository.getEntityManager().merge(existing);
        return Response.status(Response.Status.CREATED).entity(existing).build();
    }
}
