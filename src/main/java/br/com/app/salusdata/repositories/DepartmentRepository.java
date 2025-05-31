package br.com.app.salusdata.repositories;

import br.com.app.salusdata.models.DepartmentModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<DepartmentModel> {

    public Optional<DepartmentModel> findByNameIgnoreCase(String name) {
        return find("UPPER(name) = ?1", name.toUpperCase()).firstResultOptional();
    }

    public List<DepartmentModel> findByIsInternalOrderedByName(boolean type) {
        return list("FROM DepartmentModel WHERE isInternal = ?1 ORDER BY name ASC", type);
    }

    public List<DepartmentModel> findByIsActiveOrderedByName(boolean status) {
        return list("FROM DepartmentModel WHERE isActive = ?1 ORDER BY name ASC", status);
    }

    public List<DepartmentModel> findByIsInternalAndIsActiveOrderedByName(boolean type, boolean status) {
        return list("FROM DepartmentModel WHERE isInternal = ?1 AND isActive = ?2 ORDER BY name ASC", type, status);
    }

    public List<DepartmentModel> findAllOrderedByName() {
        return list("FROM DepartmentModel ORDER BY name ASC");
    }

    public List<DepartmentModel> findAllActive() {
        return list("FROM DepartmentModel WHERE isActive = true");
    }
}
