package br.com.app.salusdata.repositories;

import br.com.app.salusdata.models.UserModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<UserModel> {

    public List<UserModel> findAllByName() {
        return list("order by name asc");
    }

    public Optional<UserModel> findByRegistration(String registration) {
        return find("upper(registration) like upper(?1)", registration).firstResultOptional();
    }

    public Optional<UserModel> findByRegistrationAndEmail(String registration, String email) {
        return find("upper(registration) = upper(?1) and upper(email) = upper(?2)", registration, email).firstResultOptional();
    }

    public Optional<UserModel> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    // For queries involving ElementCollection (profiles), we must use JPQL join
    public List<UserModel> findByProfile(int profileId) {
        return list("select u from UserModel u join u.profiles p where p = ?1 order by u.name asc", profileId);
    }

    public List<UserModel> findByActiveStatus(boolean status) {
        return list("isActive = ?1 order by name asc", status);
    }

    public List<UserModel> findAllFilters(int profileId, boolean status) {
        return list("select u from UserModel u join u.profiles p where p = ?1 and u.isActive = ?2 order by u.name asc", profileId, status);
    }

    @Transactional
    public int updateUserPassword(long userId, String password) {
        // Panache does not support update queries returning entities directly,
        // so we use update with executeUpdate returning affected row count
        return update("password = ?2 where id = ?1", userId, password);
    }
}
