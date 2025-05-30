package br.com.unitins.censohgp.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tb_transfer")
public class TransferModel extends PanacheEntityBase {

    @Id
    @Column(name = "transfer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "O Usuário deve ser informado")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @NotNull(message = "O Paciente deve ser informado")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private PatientModel patient;

    @NotNull(message = "Departamento destino não pode ser nulo")
    @NotBlank(message = "Departamento destino não pode estar vazio")
    @Column(name = "destination_department", nullable = false)
    private String destinationDepartment;

    @NotNull(message = "Departamento origem não pode ser nulo")
    @NotBlank(message = "Departamento origem não pode estar vazio")
    @Column(name = "origin_department", nullable = false)
    private String originDepartment;

    private String observation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transfer_date")
    private Date transferDate;

    public TransferModel() {
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

    public String getDestinationDepartment() {
        return destinationDepartment;
    }

    public void setDestinationDepartment(String destinationDepartment) {
        this.destinationDepartment = destinationDepartment;
    }

    public String getOriginDepartment() {
        return originDepartment;
    }

    public void setOriginDepartment(String originDepartment) {
        this.originDepartment = originDepartment;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getFormattedTransferDate() {
        if (transferDate == null) return null;
        LocalDateTime date = transferDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @PrePersist
    private void updateDataBeforeInsert() {
        this.transferDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferModel that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TransferModel{" +
                "id=" + id +
                ", user=" + user +
                ", patient=" + patient +
                ", destinationDepartment='" + destinationDepartment + '\'' +
                ", originDepartment='" + originDepartment + '\'' +
                ", observation='" + observation + '\'' +
                ", transferDate=" + transferDate +
                '}';
    }
}
