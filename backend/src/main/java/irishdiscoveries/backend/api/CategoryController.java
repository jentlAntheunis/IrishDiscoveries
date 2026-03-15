package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<String> handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        String message = e.getMessage() != null ? e.getMessage() : "Unexpected error";
        if (message.toLowerCase().contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
