package vanset_backend.dto;

import jakarta.validation.constraints.NotNull;
import vanset_backend.entity.OrderStatus;

public class OrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}