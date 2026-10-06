package vanset_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Order;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUserId(
            Long id,
            Long userId
    );
}