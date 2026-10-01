package vanset_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}