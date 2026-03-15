package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.domain.Category;
import irishdiscoveries.backend.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public Category createCategory(Category category) {
        return categoryService.createCategory(category);
    }

    @PatchMapping("/{id}")
    public Category updateCategory(@PathVariable UUID id, Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
    }
}
