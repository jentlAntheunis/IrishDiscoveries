package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.Discovery;
import irishdiscoveries.backend.domain.Category;
import irishdiscoveries.backend.domain.Location;
import irishdiscoveries.backend.dto.CreateDiscoveryDto;
import irishdiscoveries.backend.dto.UpdateDiscoveryDto;
import irishdiscoveries.backend.repository.DiscoveryRepository;
import irishdiscoveries.backend.repository.CategoryRepository;
import irishdiscoveries.backend.repository.LocationRepository;
import irishdiscoveries.backend.repository.UserRepository;
import irishdiscoveries.backend.domain.User;

@Service
public class DiscoveryService implements CrudService<Discovery, CreateDiscoveryDto, UpdateDiscoveryDto> {
    private final DiscoveryRepository discoveryRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public DiscoveryService(DiscoveryRepository discoveryRepository, CategoryRepository categoryRepository,
            LocationRepository locationRepository, UserRepository userRepository) {
        this.discoveryRepository = discoveryRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
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
    public Discovery create(CreateDiscoveryDto discovery) {
        Discovery newDiscovery = new Discovery();
        if (discovery.getName() != null) {
            newDiscovery.setName(discovery.getName());
        }
        if (discovery.getDescription() != null) {
            newDiscovery.setDescription(discovery.getDescription());
        }
        if (discovery.getPriceCategory() != null) {
            newDiscovery.setPriceCategory(discovery.getPriceCategory());
        }
        if (discovery.getDiscoveredOn() != null) {
            newDiscovery.setDiscoveredOn(discovery.getDiscoveredOn());
        }
        Category category = categoryRepository.findById(discovery.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        newDiscovery.setCategory(category);

        Location location = locationRepository.findById(discovery.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        newDiscovery.setLocation(location);

        if (discovery.getUserId() != null) {
            User user = userRepository.findById(discovery.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            newDiscovery.setUser(user);
        }

        return discoveryRepository.save(newDiscovery);
    }

    @Override
    public Discovery update(UUID id, UpdateDiscoveryDto discovery) {
        Discovery existingDiscovery = getById(id);
        if (discovery.getName() != null) {
            existingDiscovery.setName(discovery.getName());
        }
        if (discovery.getDescription() != null) {
            existingDiscovery.setDescription(discovery.getDescription());
        }
        if (discovery.getLocationId() != null) {
            Location location = locationRepository.findById(discovery.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            existingDiscovery.setLocation(location);
        }
        if (discovery.getCategoryId() != null) {
            Category category = categoryRepository.findById(discovery.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingDiscovery.setCategory(category);
        }
        if (discovery.getUserId() != null) {
            User user = userRepository.findById(discovery.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingDiscovery.setUser(user);
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
