package br.com.unitins.censohgp.services;

import br.com.unitins.censohgp.models.ProcedureModel;
import br.com.unitins.censohgp.models.UserModel;
import br.com.unitins.censohgp.models.enums.Profile;
import br.com.unitins.censohgp.repositories.*;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DbInitService {

    @Inject
    UserRepository userRepository;

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    RiskFactorRepository riskFactorRepository;

    @Inject
    IncidentRepository incidentRepository;

    @Inject
    PrecautionRepository precautionRepository;

    @Inject
    PatientRepository patientRepository;

    @Inject
    ProcedureRepository procedureRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {

        if (userRepository.count() == 0) {
            List<UserModel> users = List.of(
                    //  pe.encode("123456")
                    //  new UserModel("2309952", "Brenda Martins Dias", "felipe@gmail.com", pe.encode("123456"), Profile.ADMIN)
                    new UserModel("230995", "Gabrielle Pereira Rocha", "iury@gmail.com", "123456", Profile.NURSE),
                    new UserModel("2309952", "Brenda Martins Dias", "felipe@gmail.com", "123456", Profile.ADMIN)
            );
            for (UserModel user : users) {
                userRepository.persist(user);
            }

            System.out.println("Usuários iniciais inseridos com sucesso.");
        }

        if (procedureRepository.count() == 0) {
            List<ProcedureModel> procedures = List.of(
                    new ProcedureModel("Acesso venoso central","", true),
                    new ProcedureModel("Drenagem torácica","", true),
                    new ProcedureModel("Hemodiálise","", true),
                    new ProcedureModel("SNE/SOE/SNG/SOG/GTT/Jejunostomia","", true),
                    new ProcedureModel("SVD/Cistostomia","", true),
                    new ProcedureModel("TOT/TQT","", true),
                    new ProcedureModel("Transporte de paciente","", true)
            );

            for (ProcedureModel procedure : procedures) {
                procedureRepository.persist(procedure);
            }

            System.out.println("Procedimentos iniciais inseridos com sucesso.");
        }
    }
}
