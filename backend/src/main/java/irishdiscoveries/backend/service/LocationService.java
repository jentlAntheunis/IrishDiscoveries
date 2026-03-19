package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Location;
import irishdiscoveries.backend.dto.CreateLocationDto;
import irishdiscoveries.backend.dto.UpdateLocationDto;
import irishdiscoveries.backend.repository.LocationRepository;

@Service
public class LocationService implements CrudService<Location, CreateLocationDto, UpdateLocationDto> {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location getById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() -> new RuntimeException("Location not found"));
    }

    @Override
    public Location create(CreateLocationDto location) {
        Location newLocation = new Location();
        newLocation.setCoordinates(location.getCoordinates());
        newLocation.setBoundingBox(location.getBoundingBox());
        newLocation.setPlacename(location.getPlacename());
        newLocation.setCounty(location.getCounty());
        newLocation.setPostcode(location.getPostcode());
        newLocation.setRoad(location.getRoad());

        validateLocation(newLocation);
        return locationRepository.save(newLocation);
    }

    @Override
    public Location update(UUID id, UpdateLocationDto location) {
        Location existingLocation = getById(id);
        if (location.getCoordinates() != null) {
            existingLocation.setCoordinates(location.getCoordinates());
        }
        if (location.getPlacename() != null) {
            existingLocation.setPlacename(location.getPlacename());
        }
        if (location.getCounty() != null) {
            existingLocation.setCounty(location.getCounty());
        }
//        if (location.getPostcode() != null) {
//            existingLocation.setPostcode(location.getPostcode());
//        }
//        if (location.getRoad() != null) {
//            existingLocation.setRoad(location.getRoad());
//        }
        if (location.getBoundingBox() != null) {
            existingLocation.setBoundingBox(location.getBoundingBox());
        }

        return locationRepository.save(existingLocation);
    }

    private void validateLocation(Location location) {
        if (location.getCoordinates() == null) {
            throw new IllegalArgumentException("Location coordinates are required");
        }
        if (location.getPlacename() == null || location.getPlacename().isBlank()) {
            throw new IllegalArgumentException("Location placename is required");
        }
        if (location.getCounty() == null || location.getCounty().isBlank()) {
            throw new IllegalArgumentException("Location county is required");
        }
//        if (location.getPostcode() == null || location.getPostcode().isBlank()) {
//            throw new IllegalArgumentException("Location postcode is required");
//        }
//        if (location.getRoad() == null || location.getRoad().isBlank()) {
//            throw new IllegalArgumentException("Location road is required");
//        }
        if (location.getBoundingBox() == null) {
            throw new IllegalArgumentException("Location bounding box is required");
        }
    }

    @Override
    public void delete(UUID id) {
        locationRepository.deleteById(id);
    }
}
