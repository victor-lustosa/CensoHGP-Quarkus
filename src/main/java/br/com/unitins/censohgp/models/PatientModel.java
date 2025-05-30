package br.com.unitins.censohgp.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
        import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.annotations.Cascade;

import br.com.unitins.censohgp.models.enums.Gender;

@Entity
@Table(name = "tb_patient")
public class PatientModel extends PanacheEntityBase {

    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @NotNull
    @Column(name = "medical_record", nullable = false, unique = true)
    private String medicalRecord;

    @Column(name = "name")
    private String name;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "rg")
    private String rg;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_patient_gender", joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "gender_code")
    private Set<Integer> gender = new HashSet<>();

    @Column(name = "birth_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    @JoinTable(name = "tb_patient_precaution",
            joinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "precaution_id", referencedColumnName = "precaution_id"))
    private List<PrecautionModel> precautions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private DepartmentModel department;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserModel user;

    public PatientModel() {
    }

    public PatientModel(String medicalRecord, String name, String motherName, String cpf, String rg,
                        Date birthDate, List<PrecautionModel> precautions, DepartmentModel department, UserModel user) {
        this.medicalRecord = medicalRecord;
        this.name = name;
        this.motherName = motherName;
        this.cpf = cpf;
        this.rg = rg;
        this.birthDate = birthDate;
        this.precautions = precautions != null ? precautions : new ArrayList<>();
        this.department = department;
        this.user = user;
    }

    public PatientModel(String medicalRecord, String name, String motherName, String cpf, String rg,
                        Date birthDate, List<PrecautionModel> precautions, DepartmentModel department, UserModel user, Gender addGender) {
        this(medicalRecord, name, motherName, cpf, rg, birthDate, precautions, department, user);
        addGender(addGender);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Set<Gender> getGender() {
        return gender.stream().map(Gender::toEnum).collect(Collectors.toSet());
    }

    public void addGender(Gender addGender) {
        if (addGender != null) {
            this.gender.add(addGender.getCode());
        }
    }

    public void removeGender(Gender removeGender) {
        if (removeGender != null) {
            this.gender.remove(removeGender.getCode());
        }
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<PrecautionModel> getPrecautions() {
        return precautions;
    }

    public void setPrecautions(List<PrecautionModel> precautions) {
        this.precautions = precautions;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel department) {
        this.department = department;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientModel that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(medicalRecord, that.medicalRecord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, medicalRecord);
    }

    @Override
    public String toString() {
        return "PatientModel{" +
                "id=" + id +
                ", medicalRecord='" + medicalRecord + '\'' +
                ", name='" + name + '\'' +
                ", motherName='" + motherName + '\'' +
                ", cpf='" + cpf + '\'' +
                ", rg='" + rg + '\'' +
                ", gender=" + getGender() +
                ", birthDate=" + birthDate +
                ", precautions=" + precautions +
                ", department=" + department +
                ", user=" + user +
                '}';
    }
}
