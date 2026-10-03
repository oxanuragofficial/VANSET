package vanset_backend.dto;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private Long productVariantId;
    private int quantity;
    private BigDecimal price;

    public OrderItemResponse(
            Long id,
            Long productVariantId,
            int quantity,
            BigDecimal price) {

        this.id = id;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getProductVariantId() {
        return productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}