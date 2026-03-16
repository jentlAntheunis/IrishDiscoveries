package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Discovery;
import irishdiscoveries.backend.repository.DiscoveryRepository;

@Service
public class DiscoveryService implements CrudService<Discovery> {
    private final DiscoveryRepository discoveryRepository;

    public DiscoveryService(DiscoveryRepository discoveryRepository) {
        this.discoveryRepository = discoveryRepository;
    }

    @Override
    public List<Discovery> getAll() {
        return discoveryRepository.findAll();
    }

    @Override
    public Discovery getById(UUID id) {
        return discoveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Discovery not found"));
    }

    @Override
    public Discovery create(Discovery discovery) {
        return discoveryRepository.save(discovery);
    }

    @Override
    public Discovery update(UUID id, Discovery discovery) {
        Discovery existingDiscovery = getById(id);
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

    @Override
    public void delete(UUID id) {
        discoveryRepository.deleteById(id);
    }
}
