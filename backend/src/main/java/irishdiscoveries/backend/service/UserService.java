package irishdiscoveries.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import irishdiscoveries.backend.domain.User;
import irishdiscoveries.backend.dto.UserLoginDTO;
import irishdiscoveries.backend.repository.UserRepository;

@Service
public class UserService implements CrudService<User> {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(UUID id, User user) {
        User existingUser = getById(id);
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    public User loginUser(UserLoginDTO user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            return foundUser;
        }
        throw new RuntimeException("Invalid username or password");
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
