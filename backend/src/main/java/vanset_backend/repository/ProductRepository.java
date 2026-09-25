package vanset_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}