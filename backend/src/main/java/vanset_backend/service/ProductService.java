package vanset_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vanset_backend.dto.ProductRequest;
import vanset_backend.dto.ProductResponse;
import vanset_backend.entity.Product;
import vanset_backend.exception.ProductNotFoundException;
import vanset_backend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

public ProductResponse createProduct(ProductRequest request) {

    Product product = new Product();

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setActive(request.isActive());

    Product savedProduct = productRepository.save(product);

    return new ProductResponse(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.isActive()
    );
}

public List<ProductResponse> getAllProducts() {

    return productRepository.findAll()
            .stream()
            .map(product -> new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.isActive()
            ))
            .toList();
}

public ProductResponse getProductById(Long id) {

    Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

    return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.isActive()
    );
}

public ProductResponse updateProduct(Long id, ProductRequest request) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

    existingProduct.setName(request.getName());
    existingProduct.setDescription(request.getDescription());
    existingProduct.setActive(request.isActive());

    Product savedProduct = productRepository.save(existingProduct);

    return new ProductResponse(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.isActive()
    );
}
public void deleteProduct(Long id) {
    productRepository.deleteById(id);
}
}