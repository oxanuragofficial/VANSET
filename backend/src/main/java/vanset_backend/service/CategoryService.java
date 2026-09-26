package vanset_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vanset_backend.dto.CategoryRequest;
import vanset_backend.dto.CategoryResponse;
import vanset_backend.entity.Category;
import vanset_backend.exception.CategoryInUseException;
import vanset_backend.exception.CategoryNotFoundException;
import vanset_backend.repository.CategoryRepository;
import vanset_backend.repository.ProductRepository;

@Service
public class CategoryService {

   private final CategoryRepository categoryRepository;
private final ProductRepository productRepository;

public CategoryService(
        CategoryRepository categoryRepository,
        ProductRepository productRepository) {

    this.categoryRepository = categoryRepository;
    this.productRepository = productRepository;
}

    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription(),
                savedCategory.isActive()
        );
    }

    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.isActive()
                ))
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive()
        );
    }

    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        existingCategory.setName(request.getName());
        existingCategory.setDescription(request.getDescription());
        existingCategory.setActive(request.isActive());

        Category savedCategory =
                categoryRepository.save(existingCategory);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getDescription(),
                savedCategory.isActive()
        );
    }

    public void deleteCategory(Long id) {

    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(id));

    if (productRepository.existsByCategoryId(id)) {
        throw new CategoryInUseException(id);
    }

    categoryRepository.delete(category);
}
}