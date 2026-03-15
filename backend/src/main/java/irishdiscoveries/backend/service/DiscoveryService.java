package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Discovery;
import irishdiscoveries.backend.repository.DiscoveryRepository;

@Service
public class DiscoveryService {
    private final DiscoveryRepository discoveryRepository;

    public DiscoveryService(DiscoveryRepository discoveryRepository) {
        this.discoveryRepository = discoveryRepository;
    }

    public List<Discovery> getAllDiscoveries() {
        return discoveryRepository.findAll();
    }

    public Discovery getDiscoveryById(UUID id) {
        return discoveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Discovery not found"));
    }

    public Discovery createDiscovery(Discovery discovery) {
        return discoveryRepository.save(discovery);
    }

    public Discovery updateDiscovery(UUID id, Discovery discovery) {
        Discovery existingDiscovery = getDiscoveryById(id);
        if (discovery.getName() != null) {
            existingDiscovery.setName(discovery.getName());
        }
        if (discovery.getDescription() != null) {
            existingDiscovery.setDescription(discovery.getDescription());
        }
        if (discovery.getLocation() != null) {
            existingDiscovery.setLocation(discovery.getLocation());
        }
        if (discovery.getCategory() != null) {
            existingDiscovery.setCategory(discovery.getCategory());
        }
        if (discovery.getPriceCategory() != null) {
            existingDiscovery.setPriceCategory(discovery.getPriceCategory());
        }
        if (discovery.getDiscoveredOn() != null) {
            existingDiscovery.setDiscoveredOn(discovery.getDiscoveredOn());
        }
        return discoveryRepository.save(existingDiscovery);
    }

    public void deleteDiscovery(UUID id) {
        discoveryRepository.deleteById(id);
    }
}
