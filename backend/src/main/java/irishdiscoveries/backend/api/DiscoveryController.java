package irishdiscoveries.backend.api;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.domain.Discovery;
import irishdiscoveries.backend.service.CrudService;
import irishdiscoveries.backend.service.DiscoveryService;

@RestController
@RequestMapping("/discoveries")
public class DiscoveryController extends CrudController<Discovery> {
    private final DiscoveryService discoveryService;

    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @Override
    protected CrudService<Discovery> service() {
        return discoveryService;
    }
}
