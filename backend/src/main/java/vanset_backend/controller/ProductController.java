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
import vanset_backend.dto.ProductRequest;
import vanset_backend.dto.ProductResponse;
import vanset_backend.service.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
@PostMapping("/api/products")
public ProductResponse createProduct(
        @Valid @RequestBody ProductRequest productRequest) {

    return productService.createProduct(productRequest);
}

    @GetMapping("/api/products")
public List<ProductResponse> getAllProducts() {
    return productService.getAllProducts();
}
    @GetMapping("/api/products/{id}")
public ProductResponse getProductById(@PathVariable Long id){
    return productService.getProductById(id);
}
@PutMapping("/api/products/{id}")
public ProductResponse updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductRequest productRequest) {

    return productService.updateProduct(id, productRequest);
}
@DeleteMapping("/api/products/{id}")
public void deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
}
}