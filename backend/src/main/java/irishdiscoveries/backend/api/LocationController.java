package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.service.LocationService;
import irishdiscoveries.backend.domain.Location;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable UUID id) {
        return locationService.getLocationById(id);
    }

    @PostMapping
    public Location createLocation(Location location) {
        return locationService.createLocation(location);
    }

    @PatchMapping("/{id}")
    public Location updateLocation(@PathVariable UUID id, Location location) {
        return locationService.updateLocation(id, location);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
    }
}
