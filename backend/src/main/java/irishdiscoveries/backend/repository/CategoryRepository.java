package irishdiscoveries.backend.repository;

import java.util.UUID;
import irishdiscoveries.backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
