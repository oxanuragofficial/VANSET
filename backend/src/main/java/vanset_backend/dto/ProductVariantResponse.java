package vanset_backend.dto;

import java.math.BigDecimal;

public class ProductVariantResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private int stockQuantity;
    private Long productId;

    public ProductVariantResponse(
            Long id,
            String name,
            BigDecimal price,
            int stockQuantity,
            Long productId) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public Long getProductId() {
        return productId;
    }
}