package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.service.UserService;
import irishdiscoveries.backend.domain.User;
import irishdiscoveries.backend.dto.UserLoginDTO;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable UUID id, User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody UserLoginDTO user) {
        return userService.loginUser(user);
    }
}
