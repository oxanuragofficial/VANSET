package vanset_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vanset_backend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}