package vanset_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}