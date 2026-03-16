package irishdiscoveries.backend.api;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.domain.Category;
import irishdiscoveries.backend.service.CategoryService;
import irishdiscoveries.backend.service.CrudService;

@RestController
@RequestMapping("/categories")
public class CategoryController extends CrudController<Category> {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected CrudService<Category> service() {
        return categoryService;
    }
}
