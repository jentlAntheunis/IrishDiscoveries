package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.domain.Discovery;
import irishdiscoveries.backend.service.DiscoveryService;

@RestController
@RequestMapping("/discoveries")
public class DiscoveryController {
    private final DiscoveryService discoveryService;

    public DiscoveryController(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @GetMapping
    public List<Discovery> getAllDiscoveries() {
        return discoveryService.getAllDiscoveries();
    }

    @GetMapping("/{id}")
    public Discovery getDiscoveryById(UUID id) {
        return discoveryService.getDiscoveryById(id);
    }

    @PostMapping
    public Discovery createDiscovery(Discovery discovery) {
        return discoveryService.createDiscovery(discovery);
    }

    @PatchMapping("/{id}")
    public Discovery updateDiscovery(UUID id, Discovery discovery) {
        return discoveryService.updateDiscovery(id, discovery);
    }

    @DeleteMapping("/{id}")
    public void deleteDiscovery(UUID id) {
        discoveryService.deleteDiscovery(id);
    }
}
