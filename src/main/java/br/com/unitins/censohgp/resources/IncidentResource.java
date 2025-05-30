package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.exceptions.BusinessException;
import br.com.unitins.censohgp.models.IncidentModel;
import br.com.unitins.censohgp.repositories.IncidentRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Path("/incidents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Incidentes", description = "Endpoints para incidentes")
public class IncidentResource {

    @Inject
    IncidentRepository incidentRepository;

    @GET
    public Response findAll() {
        List<IncidentModel> incidents = incidentRepository.findAllOrderedByName();
        if (incidents.isEmpty()) {
            throw new WebApplicationException("Incidente não encontrado.", Response.Status.NOT_FOUND);
        }
        return Response.ok(incidents).build();
    }

    @GET
    @Path("/actives")
    public Response findAllActive() {
        List<IncidentModel> incidents = incidentRepository.findAllActive();
        if (incidents.isEmpty()) {
            throw new WebApplicationException("Incidente não encontrado.", Response.Status.NOT_FOUND);
        }
        return Response.ok(incidents).build();
    }

    @GET
    @Path("/inactives")
    public Response findAllInactive() {
        List<IncidentModel> incidents = incidentRepository.findAllInactive();
        if (incidents.isEmpty()) {
            throw new WebApplicationException("Incidente não encontrado.", Response.Status.NOT_FOUND);
        }
        return Response.ok(incidents).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") long id) {
        IncidentModel incident = incidentRepository.findByIdOptional(id)
                .orElseThrow(() -> new WebApplicationException("Incidente não encontrado.", Response.Status.NOT_FOUND));
        return Response.ok(incident).build();
    }

    @POST
    @Transactional
    public Response create(@Valid IncidentModel incident) {
        if (incidentRepository.findByName(incident.getName()).isPresent()) {
            throw new BusinessException("Esse incidente já existe no sistema.");
        }

        incident.setActive(true);

        try {
            incidentRepository.persist(incident);
            return Response.status(Response.Status.CREATED).entity(incident).build();
        } catch (Exception e) {
            String message = Optional.ofNullable(e.getCause())
                    .map(Throwable::getCause)
                    .map(Throwable::getMessage)
                    .orElse("");

            if (message.contains("duplicate key value violates unique constraint")) {
                throw new WebApplicationException("Existe outro incidente com o mesmo nome no sistema.", Response.Status.BAD_REQUEST);
            }

            throw new WebApplicationException("Erro interno, contacte o administrador do sistema.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Transactional
    public Response update(@Valid IncidentModel incident) {
        if (incidentRepository.findByIdOptional(incident.getId()).isEmpty()) {
            throw new WebApplicationException("Incidente não encontrado.", Response.Status.NOT_FOUND);
        }

        try {
            incidentRepository.persist(incident);
            return Response.ok(incident).build();
        } catch (Exception e) {
            String message = Optional.ofNullable(e.getCause())
                    .map(Throwable::getCause)
                    .map(Throwable::getMessage)
                    .orElse("");

            if (message.contains("duplicate key value violates unique constraint")) {
                throw new WebApplicationException("Existe outro incidente com o mesmo nome no sistema.", Response.Status.BAD_REQUEST);
            }

            throw new WebApplicationException("Erro interno, contacte o administrador do sistema.", Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @PUT
    @Path("/toggle-status")
    @Transactional
    public Response toggleStatus(@Valid IncidentModel incident) {
        IncidentModel existingIncident = incidentRepository.findByIdOptional(incident.getId())
                .orElseThrow(() -> new WebApplicationException("Incidente não encontrado.", Response.Status.NOT_FOUND));

        existingIncident.setActive(!existingIncident.isActive());
        incidentRepository.persist(existingIncident);
        return Response.ok(existingIncident).build();
    }
}
