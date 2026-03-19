package irishdiscoveries.backend.api;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.service.CrudService;
import irishdiscoveries.backend.service.LocationService;
import irishdiscoveries.backend.domain.Location;
import irishdiscoveries.backend.dto.CreateLocationDto;
import irishdiscoveries.backend.dto.UpdateLocationDto;

@RestController
@RequestMapping("/locations")
public class LocationController extends CrudController<Location, CreateLocationDto, UpdateLocationDto> {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    protected CrudService<Location, CreateLocationDto, UpdateLocationDto> service() {
        return locationService;
    }
}
