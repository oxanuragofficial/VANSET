package vanset_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Address;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);

    Optional<Address> findByIdAndUserId(
            Long id,
            Long userId
    );
}