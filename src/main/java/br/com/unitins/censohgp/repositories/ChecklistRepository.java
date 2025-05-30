package br.com.unitins.censohgp.repositories;

import br.com.unitins.censohgp.models.ChecklistModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ChecklistRepository implements PanacheRepository<ChecklistModel> {

    public Optional<ChecklistModel> findByIdWithPatient(long id) {
        return find("SELECT c FROM ChecklistModel c JOIN FETCH c.patient p WHERE c.id = ?1", id)
                .firstResultOptional();
    }

    public List<ChecklistModel> findByPatientId(long patientId) {
        return list("FROM ChecklistModel WHERE patient.id = ?1", patientId);
    }
}