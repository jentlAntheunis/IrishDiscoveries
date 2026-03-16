package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Location;
import irishdiscoveries.backend.repository.LocationRepository;

@Service
public class LocationService implements CrudService<Location> {
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
    public Location create(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location update(UUID id, Location location) {
        Location existingLocation = getById(id);
        if (location.getName() != null) {
            existingLocation.setName(location.getName());
        }
        if (location.getLatitude() != null) {
            existingLocation.setLatitude(location.getLatitude());
        }
        if (location.getLongitude() != null) {
            existingLocation.setLongitude(location.getLongitude());
        }
        if (location.getCity() != null) {
            existingLocation.setCity(location.getCity());
        }
        if (location.getCounty() != null) {
            existingLocation.setCounty(location.getCounty());
        }
        if (location.getPostcode() != null) {
            existingLocation.setPostcode(location.getPostcode());
        }
        if (location.getRoad() != null) {
            existingLocation.setRoad(location.getRoad());
        }
        return locationRepository.save(existingLocation);
    }

    @Override
    public void delete(UUID id) {
        locationRepository.deleteById(id);
    }
}
