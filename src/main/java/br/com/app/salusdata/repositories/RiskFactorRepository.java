package br.com.app.salusdata.repositories;

import br.com.app.salusdata.models.RiskFactorModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RiskFactorRepository implements PanacheRepository<RiskFactorModel> {

    public Optional<RiskFactorModel> findByName(String name) {
        return find("name", name).firstResultOptional();
    }

    public List<RiskFactorModel> findAllOrderedByName() {
        return list("FROM RiskFactorModel ORDER BY name ASC");
    }

    public List<RiskFactorModel> findAllActive() {
        return list("FROM RiskFactorModel WHERE isActive = true ORDER BY name ASC");
    }

    public List<RiskFactorModel> findAllInactive() {
        return list("FROM RiskFactorModel WHERE isActive = false ORDER BY name ASC");
    }
}
