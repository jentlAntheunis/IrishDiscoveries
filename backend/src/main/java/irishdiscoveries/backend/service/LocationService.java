package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Location;
import irishdiscoveries.backend.repository.LocationRepository;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(UUID id) {
        return locationRepository.findById(id).orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(UUID id, Location location) {
        Location existingLocation = getLocationById(id);
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

    public void deleteLocation(UUID id) {
        locationRepository.deleteById(id);
    }
}
