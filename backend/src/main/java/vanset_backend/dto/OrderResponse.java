package vanset_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long id;
    private Long userId;
    private String status;
    private double totalAmount;
    private LocalDateTime createdAt;

    private String deliveryRecipientName;
    private String deliveryPhone;
    private String deliveryAddressLine;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryPincode;

    private List<OrderItemResponse> items;

    public OrderResponse(
            Long id,
            Long userId,
            String status,
            double totalAmount,
            LocalDateTime createdAt,
            String deliveryRecipientName,
            String deliveryPhone,
            String deliveryAddressLine,
            String deliveryCity,
            String deliveryState,
            String deliveryPincode,
            List<OrderItemResponse> items) {

        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;

        this.deliveryRecipientName = deliveryRecipientName;
        this.deliveryPhone = deliveryPhone;
        this.deliveryAddressLine = deliveryAddressLine;
        this.deliveryCity = deliveryCity;
        this.deliveryState = deliveryState;
        this.deliveryPincode = deliveryPincode;

        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDeliveryRecipientName() {
        return deliveryRecipientName;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public String getDeliveryAddressLine() {
        return deliveryAddressLine;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public String getDeliveryPincode() {
        return deliveryPincode;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}