package vanset_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vanset_backend.dto.ProductVariantRequest;
import vanset_backend.dto.ProductVariantResponse;
import vanset_backend.entity.Product;
import vanset_backend.entity.ProductVariant;
import vanset_backend.exception.ProductNotFoundException;
import vanset_backend.exception.ProductVariantNotFoundException;
import vanset_backend.repository.ProductRepository;
import vanset_backend.repository.ProductVariantRepository;

@Service
public class ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(
            ProductVariantRepository productVariantRepository,
            ProductRepository productRepository) {

        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    public ProductVariantResponse createVariant(
            ProductVariantRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(request.getProductId()));

        ProductVariant variant = new ProductVariant();

        variant.setName(request.getName());
        variant.setPrice(request.getPrice());
        variant.setStockQuantity(request.getStockQuantity());
        variant.setProduct(product);

        ProductVariant savedVariant =
                productVariantRepository.save(variant);

        return toResponse(savedVariant);
    }

    public List<ProductVariantResponse> getAllVariants() {

        return productVariantRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductVariantResponse getVariantById(Long id) {

        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() ->
                        new ProductVariantNotFoundException(id));

        return toResponse(variant);
    }

    public ProductVariantResponse updateVariant(
            Long id,
            ProductVariantRequest request) {

        ProductVariant existingVariant =
                productVariantRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductVariantNotFoundException(id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(request.getProductId()));

        existingVariant.setName(request.getName());
        existingVariant.setPrice(request.getPrice());
        existingVariant.setStockQuantity(request.getStockQuantity());
        existingVariant.setProduct(product);

        ProductVariant savedVariant =
                productVariantRepository.save(existingVariant);

        return toResponse(savedVariant);
    }

    public void deleteVariant(Long id) {

        if (!productVariantRepository.existsById(id)) {
            throw new ProductVariantNotFoundException(id);
        }

        productVariantRepository.deleteById(id);
    }

    private ProductVariantResponse toResponse(ProductVariant variant) {

        return new ProductVariantResponse(
                variant.getId(),
                variant.getName(),
                variant.getPrice(),
                variant.getStockQuantity(),
                variant.getProduct().getId()
        );
    }
}