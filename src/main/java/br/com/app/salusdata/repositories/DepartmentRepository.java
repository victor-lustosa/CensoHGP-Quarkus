package br.com.app.salusdata.repositories;

import br.com.app.salusdata.models.DepartmentModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<DepartmentModel> {

    public Optional<DepartmentModel> findByNameUpperCase(String name) {
        return find("UPPER(name) = ?1", name.toUpperCase()).firstResultOptional();
    }
    //averiguar qual a ordem das pesquisas anteriores. Interno, externo, ativo e inativo
    public List<DepartmentModel> findWithoutStatus(boolean type) {
        return list("FROM DepartmentModel WHERE isInternal = ?1 ORDER BY name ASC", type);
    }

    public List<DepartmentModel> findWithoutProfile(boolean status) {
        return list("FROM DepartmentModel WHERE isActive = ?1 ORDER BY name ASC", status);
    }

    public List<DepartmentModel> findWithAllFilters(boolean type, boolean status) {
        return list("FROM DepartmentModel WHERE isInternal = ?1 AND isActive = ?2 ORDER BY name ASC", type, status);
    }

    public List<DepartmentModel> findAllByName() {
        return list("FROM DepartmentModel ORDER BY name ASC");
    }

    public List<DepartmentModel> findAllActives() {
        return list("FROM DepartmentModel WHERE isActive = true");
    }
}
