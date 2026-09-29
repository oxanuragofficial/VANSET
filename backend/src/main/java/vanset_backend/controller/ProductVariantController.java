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
import vanset_backend.dto.ProductVariantRequest;
import vanset_backend.dto.ProductVariantResponse;
import vanset_backend.service.ProductVariantService;

@RestController
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    public ProductVariantController(
            ProductVariantService productVariantService) {

        this.productVariantService = productVariantService;
    }

    @PostMapping("/api/product-variants")
    public ProductVariantResponse createVariant(
            @Valid @RequestBody ProductVariantRequest request) {

        return productVariantService.createVariant(request);
    }

    @GetMapping("/api/product-variants")
    public List<ProductVariantResponse> getAllVariants() {

        return productVariantService.getAllVariants();
    }

    @GetMapping("/api/product-variants/{id}")
    public ProductVariantResponse getVariantById(
            @PathVariable Long id) {

        return productVariantService.getVariantById(id);
    }

    @PutMapping("/api/product-variants/{id}")
    public ProductVariantResponse updateVariant(
            @PathVariable Long id,
            @Valid @RequestBody ProductVariantRequest request) {

        return productVariantService.updateVariant(id, request);
    }

    @DeleteMapping("/api/product-variants/{id}")
    public void deleteVariant(@PathVariable Long id) {

        productVariantService.deleteVariant(id);
    }
}