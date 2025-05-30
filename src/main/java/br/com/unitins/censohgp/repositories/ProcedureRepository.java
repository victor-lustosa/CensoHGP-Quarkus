package br.com.unitins.censohgp.repositories;

import br.com.unitins.censohgp.models.ProcedureModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProcedureRepository implements PanacheRepository<ProcedureModel> {

    public Optional<ProcedureModel> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public List<ProcedureModel> findAllByName() {
        return list("FROM ProcedureModel ORDER BY name ASC");
    }

    public List<ProcedureModel> findAllActive() {
        return list("FROM ProcedureModel WHERE isActive = true ORDER BY name ASC");
    }

    public List<ProcedureModel> findAllInactive() {
        return list("FROM ProcedureModel WHERE isActive = false ORDER BY name ASC");
    }
}
