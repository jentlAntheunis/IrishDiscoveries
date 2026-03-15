package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllLocations() {
        try {
            List<Location> locations = locationService.getAllLocations();
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable UUID id) {
        try {
            Location location = locationService.getLocationById(id);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createLocation(@RequestBody Location location) {
        try {
            Location createdLocation = locationService.createLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLocation(@PathVariable UUID id, @RequestBody Location location) {
        try {
            Location updatedLocation = locationService.updateLocation(id, location);
            return ResponseEntity.ok(updatedLocation);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable UUID id) {
        try {
            locationService.deleteLocation(id);
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
