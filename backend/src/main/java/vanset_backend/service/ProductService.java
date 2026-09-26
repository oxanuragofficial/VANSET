package vanset_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vanset_backend.dto.ProductRequest;
import vanset_backend.dto.ProductResponse;
import vanset_backend.entity.Category;
import vanset_backend.entity.Product;
import vanset_backend.exception.CategoryNotFoundException;
import vanset_backend.exception.ProductNotFoundException;
import vanset_backend.repository.CategoryRepository;
import vanset_backend.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setActive(request.isActive());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.isActive(),
                savedProduct.getCategory().getId()
        );
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.isActive(),
                        product.getCategory().getId()
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
                product.isActive(),
                product.getCategory().getId()
        );
    }

   public ProductResponse updateProduct(
        Long id,
        ProductRequest request) {

    Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));

    Category category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() ->
                    new CategoryNotFoundException(request.getCategoryId()));

    existingProduct.setName(request.getName());
    existingProduct.setDescription(request.getDescription());
    existingProduct.setActive(request.isActive());
    existingProduct.setCategory(category);

    Product savedProduct = productRepository.save(existingProduct);

    return new ProductResponse(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.isActive(),
            savedProduct.getCategory().getId()
    );
}

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}