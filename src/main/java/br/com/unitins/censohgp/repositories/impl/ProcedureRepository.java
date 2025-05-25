package br.com.unitins.censohgp.repositories.impl;

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

    public List<ProcedureModel> findAllOrdered() {
        return find("ORDER BY name ASC").list();
    }

    public List<ProcedureModel> findAllActive() {
        return find("active = true ORDER BY name ASC").list();
    }

    public List<ProcedureModel> findAllInactive() {
        return find("active = false ORDER BY name ASC").list();
    }
}
