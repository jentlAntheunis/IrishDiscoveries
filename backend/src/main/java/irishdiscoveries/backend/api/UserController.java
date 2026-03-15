package irishdiscoveries.backend.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        try {
            User user = userService.loginUser(dto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.ok(user);
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
