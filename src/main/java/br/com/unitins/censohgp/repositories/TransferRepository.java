package br.com.unitins.censohgp.repositories;

import br.com.unitins.censohgp.models.TransferModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransferRepository implements PanacheRepository<TransferModel> {

    public List<TransferModel> findByPatientId(long patientId) {
        return list("patient.id = ?1 ORDER BY transferDate DESC", patientId);
    }
}