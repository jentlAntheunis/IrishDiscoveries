package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Category;
import irishdiscoveries.backend.dto.CreateCategoryDto;
import irishdiscoveries.backend.dto.UpdateCategoryDto;
import irishdiscoveries.backend.repository.CategoryRepository;

@Service
public class CategoryService implements CrudService<Category, CreateCategoryDto, UpdateCategoryDto> {
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
    public Category create(CreateCategoryDto category) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category update(UUID id, UpdateCategoryDto category) {
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
