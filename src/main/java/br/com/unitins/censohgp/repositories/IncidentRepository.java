package br.com.unitins.censohgp.repositories;

import br.com.unitins.censohgp.models.IncidentModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class IncidentRepository implements PanacheRepository<IncidentModel> {

    public Optional<IncidentModel> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public List<IncidentModel> findAllOrderedByName() {
        return list("FROM IncidentModel ORDER BY name ASC");
    }

    public List<IncidentModel> findAllActive() {
        return list("FROM IncidentModel WHERE isActive = true ORDER BY name ASC");
    }

    public List<IncidentModel> findAllInactive() {
        return list("FROM IncidentModel WHERE isActive = false ORDER BY name ASC");
    }
}
