package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllDiscoveries() {
        try {
            List<Discovery> discoveries = discoveryService.getAllDiscoveries();
            return ResponseEntity.ok(discoveries);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscoveryById(@PathVariable UUID id) {
        try {
            Discovery discovery = discoveryService.getDiscoveryById(id);
            return ResponseEntity.ok(discovery);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createDiscovery(@RequestBody Discovery discovery) {
        try {
            Discovery createdDiscovery = discoveryService.createDiscovery(discovery);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscovery);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateDiscovery(@PathVariable UUID id, @RequestBody Discovery discovery) {
        try {
            Discovery updatedDiscovery = discoveryService.updateDiscovery(id, discovery);
            return ResponseEntity.ok(updatedDiscovery);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscovery(@PathVariable UUID id) {
        try {
            discoveryService.deleteDiscovery(id);
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
