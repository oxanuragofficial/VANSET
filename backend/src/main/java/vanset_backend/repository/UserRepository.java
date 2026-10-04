package vanset_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.User;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneOrEmail(
            String phone,
            String email
    );
}