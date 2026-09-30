package vanset_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vanset_backend.dto.CollectionRequest;
import vanset_backend.dto.CollectionResponse;
import vanset_backend.service.CollectionService;

@RestController
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping("/api/collections")
    public CollectionResponse createCollection(
            @Valid @RequestBody CollectionRequest request) {

        return collectionService.createCollection(request);
    }

    @GetMapping("/api/collections")
    public List<CollectionResponse> getAllCollections() {

        return collectionService.getAllCollections();
    }

    @GetMapping("/api/collections/{id}")
    public CollectionResponse getCollectionById(
            @PathVariable Long id) {

        return collectionService.getCollectionById(id);
    }

    @PutMapping("/api/collections/{id}")
    public CollectionResponse updateCollection(
            @PathVariable Long id,
            @Valid @RequestBody CollectionRequest request) {

        return collectionService.updateCollection(id, request);
    }

    @DeleteMapping("/api/collections/{id}")
    public void deleteCollection(
            @PathVariable Long id) {

        collectionService.deleteCollection(id);
    }

    @PostMapping("/api/collections/{collectionId}/products/{productId}")
    public void addProductToCollection(
            @PathVariable Long collectionId,
            @PathVariable Long productId) {

        collectionService.addProductToCollection(
                collectionId,
                productId
        );
        
    }
    @DeleteMapping("/api/collections/{collectionId}/products/{productId}")
public void removeProductFromCollection(
        @PathVariable Long collectionId,
        @PathVariable Long productId) {

    collectionService.removeProductFromCollection(
            collectionId,
            productId
    );
}
}