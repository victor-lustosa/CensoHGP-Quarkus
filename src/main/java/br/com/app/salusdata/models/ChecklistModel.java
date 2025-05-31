package br.com.app.salusdata.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_checklist")
public class ChecklistModel extends PanacheEntityBase {

    @Id
    @Column(name = "checklist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
    private PatientModel patient;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_checklist_risk_factor",
            joinColumns = @JoinColumn(name = "checklist_id", referencedColumnName = "checklist_id"),
            inverseJoinColumns = @JoinColumn(name = "risk_factor_id", referencedColumnName = "risk_factor_id")
    )
    private List<RiskFactorModel> riskFactors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_checklist_incident",
            joinColumns = @JoinColumn(name = "checklist_id", referencedColumnName = "checklist_id"),
            inverseJoinColumns = @JoinColumn(name = "incident_id", referencedColumnName = "incident_id")
    )
    private List<IncidentModel> incidents = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_checklist_procedure",
            joinColumns = @JoinColumn(name = "checklist_id", referencedColumnName = "checklist_id"),
            inverseJoinColumns = @JoinColumn(name = "procedure_id", referencedColumnName = "procedure_id")
    )
    private List<ProcedureModel> procedures = new ArrayList<>();

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "observation")
    private String observation;

    public ChecklistModel() {
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public String getFormattedCreatedAt() {
        if (createdAt == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return this.createdAt.format(formatter);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public List<RiskFactorModel> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<RiskFactorModel> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public List<IncidentModel> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<IncidentModel> incidents) {
        this.incidents = incidents;
    }

    public List<ProcedureModel> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<ProcedureModel> procedures) {
        this.procedures = procedures;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public String toString() {
        return "ChecklistModel{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", patient=" + (patient != null ? patient.getId() : null) +
                ", createdAt=" + createdAt +
                ", observation='" + observation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChecklistModel that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
