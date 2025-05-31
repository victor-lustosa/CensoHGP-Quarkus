package br.com.app.salusdata.repositories;

import br.com.app.salusdata.models.PrecautionModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PrecautionRepository implements PanacheRepository<PrecautionModel> {

    public Optional<PrecautionModel> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public List<PrecautionModel> findAllOrdered() {
        return list("FROM PrecautionModel ORDER BY name ASC");
    }

    public List<PrecautionModel> findAllActive() {
        return list("FROM PrecautionModel WHERE isActive = true ORDER BY name ASC");
    }

    public List<PrecautionModel> findAllInactive() {
        return list("FROM PrecautionModel WHERE isActive = false ORDER BY name ASC");
    }
}
