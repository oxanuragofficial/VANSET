package vanset_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CollectionRequest {

    @NotBlank(message = "Collection name is required")
    private String name;

    private String description;

    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}