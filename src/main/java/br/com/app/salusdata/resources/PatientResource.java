package br.com.app.salusdata.resources;

import br.com.app.salusdata.models.DepartmentModel;
import br.com.app.salusdata.models.PatientModel;
import br.com.app.salusdata.models.PrecautionModel;
import br.com.app.salusdata.models.UserModel;
import br.com.app.salusdata.repositories.DepartmentRepository;
import br.com.app.salusdata.repositories.PatientRepository;
import br.com.app.salusdata.repositories.PrecautionRepository;
import br.com.app.salusdata.repositories.UserRepository;
        import br.com.app.salusdata.models.dtos.NewPatientDTO;
import br.com.app.salusdata.models.dtos.PatientDTO;
import br.com.app.salusdata.models.enums.Gender;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
        import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Path("/patients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PatientResource {

    @Inject
    PatientRepository patientRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    PrecautionRepository precautionRepository;

    @Inject
    UserRepository userRepository;

    @GET
    public List<PatientModel> findAll() {
        List<PatientModel> patients = patientRepository.listAll();
        patients.sort(Comparator.comparing(PatientModel::getName, String.CASE_INSENSITIVE_ORDER));
        return patients;
    }

    @GET
    @Path("/{id}")
    public PatientModel findById(@RestPath long id) {
        return patientRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));
    }

    @GET
    @Path("/department/{name}")
    public List<PatientModel> findByDepartment(@RestPath String name) {
        DepartmentModel department = departmentRepository.findByNameUpperCase(name)
                .orElseThrow(() -> new BadRequestException("Invalid department"));

        return patientRepository.findByDepartmentId(department.getId());
    }

    @POST
    @Transactional
    @Path("/{userRegistration}")
    public Response create(@Valid NewPatientDTO dto, @RestPath String userRegistration) {

        if (patientRepository.findByMedicalRecord(dto.medicalRecord()).isPresent()) {
            throw new BadRequestException("Paciente com o prontuário " + dto.medicalRecord() + " já existe.");
        }

        DepartmentModel department = departmentRepository.findByIdOptional(dto.departmentId())
                .orElseThrow(() -> new BadRequestException("Departamento não encontrado."));

        UserModel user = userRepository.findByRegistration(userRegistration)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado."));

        List<PrecautionModel> precautions = new ArrayList<>();
        if (dto.precautions() != null) {
            for (Long id : dto.precautions()) {
                PrecautionModel precaution = precautionRepository.findByIdOptional(id)
                        .orElseThrow(() -> new BadRequestException("Precaução com ID " + id + " não encontrada."));
                precautions.add(precaution);
            }
        }

        PatientModel patient = new PatientModel();
        patient.setName(dto.name());
        patient.setMedicalRecord(dto.medicalRecord());
        patient.setMotherName(dto.motherName());
        patient.setCpf(dto.cpf());
        patient.setRg(dto.rg());
        patient.setUser(user);
        patient.setBirthDate(dto.birthDate());
        patient.setDepartment(department);
        patient.setPrecautions(precautions);

        patient.getGender().clear();
        if ("Masculino".equalsIgnoreCase(dto.gender())) {
            patient.addGender(Gender.MALE);
        } else if ("Feminino".equalsIgnoreCase(dto.gender())) {
            patient.addGender(Gender.FEMALE);
        }

        patientRepository.persist(patient);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Transactional
    @Path("/{userRegistration}")
    public Response updatePatient(@Valid PatientDTO dto, @RestPath String userRegistration) {

        PatientModel patient = patientRepository.findByIdOptional(dto.patientId())
                .orElseThrow(() -> new NotFoundException("Paciente não encontrado."));

        DepartmentModel department = departmentRepository.findByIdOptional(dto.departmentId())
                .orElseThrow(() -> new BadRequestException("Departamento não encontrado."));

        UserModel user = userRepository.findByRegistration(userRegistration)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado."));

        List<PrecautionModel> precautions = new ArrayList<>();
        if (dto.precautions() != null) {
            for (Long id : dto.precautions()) {
                PrecautionModel precaution = precautionRepository.findByIdOptional(id)
                        .orElseThrow(() -> new BadRequestException("Precaução com ID " + id + " não encontrada."));
                precautions.add(precaution);
            }
        }

        if (dto.name() != null) patient.setName(dto.name());
        if (dto.rg() != null) patient.setRg(dto.rg());
        if (dto.cpf() != null) patient.setCpf(dto.cpf());
        if (dto.motherName() != null) patient.setMotherName(dto.motherName());
        if (dto.birthDate() != null) patient.setBirthDate(dto.birthDate());

        patient.setUser(user);
        patient.setDepartment(department);
        patient.setPrecautions(precautions);

        patient.getGender().clear();
        if ("Masculino".equalsIgnoreCase(dto.gender())) {
            patient.addGender(Gender.MALE);
        } else if ("Feminino".equalsIgnoreCase(dto.gender())) {
            patient.addGender(Gender.FEMALE);
        }

        patientRepository.persist(patient);
        return Response.ok().build();
    }
}
