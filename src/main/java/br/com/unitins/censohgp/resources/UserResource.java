package br.com.unitins.censohgp.resources;

import br.com.unitins.censohgp.exceptions.BusinessException;
import br.com.unitins.censohgp.models.UserModel;
import br.com.unitins.censohgp.models.dtos.UserDTO;
import br.com.unitins.censohgp.models.enums.Profile;
import br.com.unitins.censohgp.repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Usuários", description = "Endpoints para usuários do sistema")
public class UserResource {

    private static final Logger LOG = Logger.getLogger(UserResource.class);

    @Inject
    UserRepository userRepository;

    @GET
    public Response findAll() {
        List<UserModel> users = userRepository.findAllByName();
        return Response.ok(users).build();
    }

    @GET
    @Path("/registration/{registration}")
    public Response findByRegistration(@PathParam("registration") String registration) {
        UserModel user = userRepository.findByRegistration(registration)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        return Response.ok(user).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") long id) {
        UserModel user = userRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        return Response.ok(user).build();
    }

    @POST
    @Transactional
    public Response save(@Valid UserDTO userDto) {
        if (userRepository.findByRegistration(userDto.registration()).isPresent()) {
            throw new BadRequestException("Esta matrícula já existe no sistema!");
        }
        try {
            UserModel user = new UserModel();
            user.setRegistration(userDto.registration());
            user.setName(userDto.name());
            user.setEmail(userDto.email());
            user.setActive(true);
            //user.setPassword(BCrypt.hashpw(userDto.password(), BCrypt.gensalt()));
            user.setPassword(userDto.password());

            switch (userDto.profile()) {
                case "NURSE" -> user.addProfile(Profile.NURSE);
                case "ADMIN" -> user.addProfile(Profile.ADMIN);
                default -> throw new BusinessException("Esse perfil não existe no sistema!");
            }
            userRepository.persist(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (Exception e) {
            LOG.error("Error saving user", e);
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
        }
    }

    @PUT
    @Transactional
    public Response updateUser(@Valid UserDTO userDto) {
        UserModel user = userRepository.findByRegistration(userDto.registration())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (user.getId() != userDto.id() && userRepository.findByRegistration(userDto.registration()).isPresent()) {
            throw new BadRequestException("Esta matrícula já existe no sistema!");
        }

        try {
            user.setId(userDto.id());
            user.setRegistration(userDto.registration());
            user.setName(userDto.name());
            user.setEmail(userDto.email());
            user.setActive(userDto.isActive());

            if (userDto.password() != null && !userDto.password().isEmpty()) {
                //user.setPassword(BCrypt.hashpw(userDto.password(), BCrypt.gensalt()));
                user.setPassword(userDto.password());
            }

            user.getProfiles().clear();

            switch (userDto.profile()) {
                case "ADMIN" -> user.addProfile(Profile.ADMIN);
                case "NURSE" -> user.addProfile(Profile.NURSE);
                default -> throw new BusinessException("Esse perfil não existe no sistema!");
            }

            userRepository.persist(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (Exception e) {
            LOG.error("Error updating user", e);
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
        }
    }

    @GET
    @Path("/{profile}/{status}")
    public Response getByFilters(@PathParam("profile") String profile,
                                 @PathParam("status") String status) {
        List<UserModel> result = new ArrayList<>();

        boolean hasProfile = profile != null && !profile.isEmpty();
        boolean hasStatus = status != null && !status.isEmpty();

        if (hasProfile && !hasStatus) {
            int roleId = Profile.findIdByName(profile);
            result = userRepository.findByProfile(roleId);
        } else if (!hasProfile && hasStatus) {
            boolean statusBoolean = Boolean.parseBoolean(status);
            result = userRepository.findByActiveStatus(statusBoolean);
        } else if (hasProfile) {
            int roleId = Profile.findIdByName(profile);
            boolean statusBoolean = Boolean.parseBoolean(status);
            result = userRepository.findAllFilters(roleId, statusBoolean);
        }

        return Response.ok(result).build();
    }

    @PUT
    @Path("/toggle-status/{registration}")
    @Transactional
    public Response updateStatusUser(@Valid UserDTO userDto,
                                     @PathParam("registration") String registration) {
        UserModel user = userRepository.findByIdOptional(userDto.id())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (registration.equals(user.getRegistration())) {
            throw new ForbiddenException("Você não pode se desativar!");
        }

        try {
            user.setActive(!user.isActive());
            userRepository.persist(user);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (Exception e) {
            LOG.error("Error toggling user status", e);
            throw new InternalServerErrorException("Erro interno, contacte o administrador do sistema!");
        }
    }
}
