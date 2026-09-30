package vanset_backend.dto;

import java.util.List;

public class CollectionResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private List<ProductResponse> products;

    public CollectionResponse(
            Long id,
            String name,
            String description,
            boolean active,
            List<ProductResponse> products) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }
}