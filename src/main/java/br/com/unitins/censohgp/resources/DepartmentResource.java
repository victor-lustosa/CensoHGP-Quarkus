package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.models.DepartmentModel;
import br.com.unitins.censohgp.models.dtos.DepartmentDTO;
import br.com.unitins.censohgp.repositories.DepartmentRepository;
import br.com.unitins.censohgp.exceptions.BusinessException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Collections;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Departamentos", description = "Endpoints para departamentos")
@RequestScoped
public class DepartmentResource {

    @Inject
    DepartmentRepository departmentRepository;

    @GET
    public Response findAll() {
        return Response.ok(departmentRepository.findAllByName()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") long id) {
        DepartmentModel department = departmentRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Departamento não encontrado."));
        return Response.ok(department).build();
    }

    @GET
    @Path("/actives")
    public Response findAllActive() {
        return Response.ok(departmentRepository.findAllActives()).build();
    }

    @GET
    @Path("/filter")
    public Response getByFilters(@QueryParam("type") String type, @QueryParam("status") String status) {
        if (!type.isEmpty() && status.isEmpty()) {
            boolean typeBoolean = Boolean.parseBoolean(type);
            return Response.ok(departmentRepository.findWithoutStatus(typeBoolean)).build();
        } else if (type.isEmpty() && !status.isEmpty()) {
            boolean statusBoolean = Boolean.parseBoolean(status);
            return Response.ok(departmentRepository.findWithoutProfile(statusBoolean)).build();
        } else if (!type.isEmpty()) {
            boolean typeBoolean = Boolean.parseBoolean(type);
            boolean statusBoolean = Boolean.parseBoolean(status);
            return Response.ok(departmentRepository.findWithAllFilters(typeBoolean, statusBoolean)).build();
        }
        return Response.ok(Collections.emptyList()).build();
    }

    @POST
    @Transactional
    public Response create(@Valid DepartmentDTO dto) {
        if (dto.bedsCount() < 0) {
            throw new BusinessException("Numero de leitos deve ser maior que zero.");
        }

        if (departmentRepository.findByNameUpperCase(dto.name()).isPresent()) {
            throw new BusinessException("Esse departamento já existe no sistema.");
        }

        DepartmentModel department = new DepartmentModel();
        department.setName(dto.name());
        department.setBedsCount(dto.bedsCount());
        department.setDescription(dto.description());
        department.setActive(true);
        department.setInternal(dto.isInternal());

        departmentRepository.persist(department);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Transactional
    public Response update(@Valid DepartmentDTO dto) {
        departmentRepository.findByNameUpperCase(dto.name())
                .filter(dep -> dep.getId() != dto.departmentId())
                .ifPresent(dep -> {
                    throw new BusinessException("Esse departamento já existe no sistema.");
                });

        DepartmentModel department = departmentRepository.findByIdOptional(dto.departmentId())
                .orElseThrow(() -> new NotFoundException("Departamento não encontrado."));

        if (dto.bedsCount() < 0) {
            throw new BusinessException("Numero de leitos deve ser maior que zero.");
        }

        department.setName(dto.name());
        department.setBedsCount(dto.bedsCount());
        department.setDescription(dto.description());
        department.setActive(dto.isActive());
        department.setInternal(dto.isInternal());

        departmentRepository.persist(department);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/toggle-status")
    @Transactional
    public Response toggleStatus(@Valid DepartmentDTO dto) {
        DepartmentModel department = departmentRepository.findByIdOptional(dto.departmentId())
                .orElseThrow(() -> new NotFoundException("Departamento não encontrado."));

        department.setActive(!department.isActive());
        departmentRepository.persist(department);
        return Response.status(Response.Status.CREATED).build();
    }
}
