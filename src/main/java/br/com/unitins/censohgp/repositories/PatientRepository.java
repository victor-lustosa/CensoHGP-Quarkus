package br.com.unitins.censohgp.repositories;

import br.com.unitins.censohgp.models.PatientModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PatientRepository implements PanacheRepository<PatientModel> {

    public Optional<PatientModel> findByMedicalRecord(String medicalRecord) {
        return find("medicalRecord", medicalRecord).firstResultOptional();
    }

    public List<PatientModel> findByDepartmentId(long departmentId) {
        return list("FROM PatientModel WHERE department.id = ?1", departmentId);
    }
}
