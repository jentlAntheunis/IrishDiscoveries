package irishdiscoveries.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import irishdiscoveries.backend.domain.Discovery;

public interface DiscoveryRepository extends JpaRepository<Discovery, UUID> {
    // public List<Discovery> findByLocationId(UUID locationId);

    // public List<Discovery> findByCategoryId(UUID categoryId);

    public List<Discovery> findByUserId(UUID userId);
}
