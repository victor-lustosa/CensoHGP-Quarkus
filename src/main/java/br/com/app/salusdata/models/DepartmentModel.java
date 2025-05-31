package br.com.app.salusdata.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_department")
public class DepartmentModel extends PanacheEntityBase {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "beds_count", nullable = false)
    private Integer bedsCount;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "is_internal", nullable = false)
    private Boolean isInternal;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public DepartmentModel() {
    }

    public DepartmentModel(String name, Integer bedsCount, String description, Boolean isInternal, Boolean isActive) {
        this.name = name;
        this.bedsCount = bedsCount;
        this.description = description;
        this.isInternal = isInternal;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBedsCount() {
        return bedsCount;
    }

    public void setBedsCount(Integer bedsCount) {
        this.bedsCount = bedsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isInternal() {
        return isInternal;
    }

    public void setInternal(Boolean isInternal) {
        this.isInternal = isInternal;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
