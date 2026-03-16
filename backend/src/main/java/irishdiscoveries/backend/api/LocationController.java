package irishdiscoveries.backend.api;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.service.CrudService;
import irishdiscoveries.backend.service.LocationService;
import irishdiscoveries.backend.domain.Location;

@RestController
@RequestMapping("/locations")
public class LocationController extends CrudController<Location> {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    protected CrudService<Location> service() {
        return locationService;
    }
}
