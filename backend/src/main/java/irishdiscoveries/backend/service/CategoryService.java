package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Category;
import irishdiscoveries.backend.repository.CategoryRepository;

@Service
public class CategoryService implements CrudService<Category> {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UUID id, Category category) {
        Category existingCategory = getById(id);
        if (category.getName() != null) {
            existingCategory.setName(category.getName());
        }
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }

}
