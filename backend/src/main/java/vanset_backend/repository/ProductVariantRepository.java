package vanset_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanset_backend.entity.ProductVariant;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long> {
}