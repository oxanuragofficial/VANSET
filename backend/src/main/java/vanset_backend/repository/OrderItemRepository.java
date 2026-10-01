package vanset_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);
}