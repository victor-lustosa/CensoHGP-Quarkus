package br.com.unitins.censohgp.repositories;

import br.com.unitins.censohgp.models.RiskFactorModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RiskFactorRepository implements PanacheRepository<RiskFactorModel> {

    public RiskFactorModel findByName(String name) {
        return find("name", name).firstResult();
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
