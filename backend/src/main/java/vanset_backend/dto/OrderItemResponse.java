package vanset_backend.dto;

public class OrderItemResponse {

    private Long id;
    private Long productVariantId;
    private int quantity;
    private double price;

    public OrderItemResponse(
            Long id,
            Long productVariantId,
            int quantity,
            double price) {

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

    public double getPrice() {
        return price;
    }
}