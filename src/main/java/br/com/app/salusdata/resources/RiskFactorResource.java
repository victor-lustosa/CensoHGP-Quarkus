/*package br.com.app.salusdata.resources;

import br.com.app.salusdata.exceptions.BusinessException;
import br.com.app.salusdata.models.RiskFactorModel;
import br.com.app.salusdata.repositories.RiskFactorRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/risk-factors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Fatores de Risco", description = "Endpoints para fatores de risco")
public class RiskFactorResource {

    private final RiskFactorRepository riskFactorRepository;

    public RiskFactorResource(RiskFactorRepository riskFactorRepository) {
        this.riskFactorRepository = riskFactorRepository;
    }

    @GET
    public Response findAll() {
        List<RiskFactorModel> list = riskFactorRepository.findAllOrderedByName();
        if (list.isEmpty()) {
            throw new NotFoundException("Fator de risco não encontrado.");
        }
        return Response.ok(list).build();
    }

    @GET
    @Path("/actives")
    public Response findAllActive() {
        List<RiskFactorModel> list = riskFactorRepository.findAllActive();
        if (list.isEmpty()) {
            throw new NotFoundException("Fator de risco não encontrado.");
        }
        return Response.ok(list).build();
    }

    @GET
    @Path("/inactives")
    public Response findAllInactive() {
        List<RiskFactorModel> list = riskFactorRepository.findAllInactive();
        if (list.isEmpty()) {
            throw new NotFoundException("Fator de risco não encontrado.");
        }
        return Response.ok(list).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        RiskFactorModel riskFactor = riskFactorRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Fator de risco não encontrado."));
        return Response.ok(riskFactor).build();
    }

    @POST
    @Transactional
    public Response create(@Valid RiskFactorModel riskFactor) {
        if (riskFactorRepository.findByName(riskFactor.getName()).isPresent()) {
            throw new BusinessException("Fator de risco com esse nome já existe no sistema.");
        }
        riskFactor.setActive(true);
        return Response.status(Response.Status.CREATED).entity(riskFactorRepository.persistAndFlush(riskFactor)).build();
    }

    @PUT
    @Transactional
    public Response update(@Valid RiskFactorModel riskFactor) {
        if (riskFactorRepository.findByIdOptional(riskFactor.getId()).isEmpty()) {
            throw new BadRequestException("Fator de risco não encontrado");
        }
        try {
            return Response.ok(riskFactorRepository.getEntityManager().merge(riskFactor)).build();
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null &&
                    e.getCause().getCause().getMessage().contains("duplicate key")) {
                throw new BadRequestException("Um fator de risco com esse nome já existe no sistema.");
            }
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema.");
        }
    }

    @PUT
    @Path("/toggle-status")
    @Transactional
    public Response toggleStatus(@Valid RiskFactorModel riskFactor) {
        RiskFactorModel existing = riskFactorRepository.findByIdOptional(riskFactor.getId())
                .orElseThrow(() -> new BadRequestException("Fator de risco não encontrado."));

        existing.setActive(!existing.isActive());
        try {
            return Response.ok(riskFactorRepository.getEntityManager().merge(existing)).build();
        } catch (Exception e) {
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema.");
        }
    }
}
*/