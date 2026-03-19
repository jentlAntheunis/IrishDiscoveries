package irishdiscoveries.backend.api;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.domain.Discovery;
import irishdiscoveries.backend.dto.CreateDiscoveryDto;
import irishdiscoveries.backend.dto.UpdateDiscoveryDto;
import irishdiscoveries.backend.service.CrudService;
import irishdiscoveries.backend.service.DiscoveryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discoveries")
public class DiscoveryController extends CrudController<Discovery, CreateDiscoveryDto, UpdateDiscoveryDto> {
    private final DiscoveryService discoveryService;

    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Override
    protected CrudService<Discovery, CreateDiscoveryDto, UpdateDiscoveryDto> service() {
        return discoveryService;
    }


    @GetMapping("/by-userid/{userId}")
    public List<Discovery> getByUserId(@PathVariable UUID userId) {
        return discoveryService.getByUserId(userId);
    }
}
