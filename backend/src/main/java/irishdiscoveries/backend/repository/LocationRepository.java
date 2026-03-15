package irishdiscoveries.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import irishdiscoveries.backend.domain.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

}
