package vanset_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Address;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);
}