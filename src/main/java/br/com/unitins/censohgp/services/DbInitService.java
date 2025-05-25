package br.com.unitins.censohgp.services;

import br.com.unitins.censohgp.models.ProcedureModel;
import br.com.unitins.censohgp.repositories.impl.ProcedureRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class DbInitService {

    @Inject
    ProcedureRepository procedureRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
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

            System.out.println("Procedures iniciais inseridos com sucesso.");
        }
    }
}
