package vanset_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vanset_backend.dto.CollectionRequest;
import vanset_backend.dto.CollectionResponse;
import vanset_backend.dto.ProductResponse;
import vanset_backend.entity.Collection;
import vanset_backend.entity.Product;
import vanset_backend.exception.CollectionNotFoundException;
import vanset_backend.exception.ProductNotFoundException;
import vanset_backend.repository.CollectionRepository;
import vanset_backend.repository.ProductRepository;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final ProductRepository productRepository;

    public CollectionService(
            CollectionRepository collectionRepository,
            ProductRepository productRepository) {

        this.collectionRepository = collectionRepository;
        this.productRepository = productRepository;
    }

    public CollectionResponse createCollection(CollectionRequest request) {

        Collection collection = new Collection();

        collection.setName(request.getName());
        collection.setDescription(request.getDescription());
        collection.setActive(request.isActive());

        Collection savedCollection =
                collectionRepository.save(collection);

        return toResponse(savedCollection);
    }

    public List<CollectionResponse> getAllCollections() {

        return collectionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CollectionResponse getCollectionById(Long id) {

        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() ->
                        new CollectionNotFoundException(id));

        return toResponse(collection);
    }

    public CollectionResponse updateCollection(
            Long id,
            CollectionRequest request) {

        Collection existingCollection =
                collectionRepository.findById(id)
                        .orElseThrow(() ->
                                new CollectionNotFoundException(id));

        existingCollection.setName(request.getName());
        existingCollection.setDescription(request.getDescription());
        existingCollection.setActive(request.isActive());

        Collection savedCollection =
                collectionRepository.save(existingCollection);

        return toResponse(savedCollection);
    }

    public void deleteCollection(Long id) {

        if (!collectionRepository.existsById(id)) {
            throw new CollectionNotFoundException(id);
        }

        collectionRepository.deleteById(id);
    }

    public void addProductToCollection(
            Long collectionId,
            Long productId) {

        Collection collection =
                collectionRepository.findById(collectionId)
                        .orElseThrow(() ->
                                new CollectionNotFoundException(collectionId));

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException(productId));

        collection.getProducts().add(product);

        collectionRepository.save(collection);
    }

    public void removeProductFromCollection(
            Long collectionId,
            Long productId) {

        Collection collection =
                collectionRepository.findById(collectionId)
                        .orElseThrow(() ->
                                new CollectionNotFoundException(collectionId));

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException(productId));

        collection.getProducts().remove(product);

        collectionRepository.save(collection);
    }

    private CollectionResponse toResponse(Collection collection) {

        List<ProductResponse> products =
                collection.getProducts()
                        .stream()
                        .map(product -> new ProductResponse(
                                product.getId(),
                                product.getName(),
                                product.getDescription(),
                                product.isActive(),
                                product.getCategory().getId()
                        ))
                        .toList();

        return new CollectionResponse(
                collection.getId(),
                collection.getName(),
                collection.getDescription(),
                collection.isActive(),
                products
        );
    }
}