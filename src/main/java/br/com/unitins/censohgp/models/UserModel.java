package br.com.unitins.censohgp.models;

import br.com.unitins.censohgp.models.enums.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_user")
public class UserModel extends PanacheEntityBase {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "registration", nullable = false, unique = true)
    private String registration;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_user_profile", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "profile")
    private Set<Integer> profiles = new HashSet<>();

    @JsonIgnore
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public UserModel() {
    }

    public UserModel(@NotBlank @NotNull String registration, @NotBlank String name,
                     @NotBlank String email, @NotBlank String password) {
        this.registration = registration;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = true;
    }

    public UserModel(@NotBlank @NotNull String registration, @NotBlank String name,
                     @NotBlank String email, @NotBlank String password, Profile profile) {
        this(registration, name, email, password);
        this.profiles.add(profile.getCode());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void addProfile(Profile profile) {
        profiles.add(profile.getCode());
    }

    public void removeProfile(Profile profile) {
        profiles.remove(profile.getCode());
    }

    public Set<Profile> getProfiles() {
        return profiles.stream()
                .map(Profile::toEnum)
                .collect(Collectors.toSet());
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Integer> getProfilesRaw() {
        return profiles;
    }

    public void setProfiles(Set<Integer> profiles) {
        this.profiles = profiles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", registration='" + registration + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profiles=" + profiles +
                ", isActive=" + isActive +
                '}';
    }
}
