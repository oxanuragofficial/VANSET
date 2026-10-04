package vanset_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);
}