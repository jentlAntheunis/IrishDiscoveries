package irishdiscoveries.backend.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import irishdiscoveries.backend.service.CrudService;
import irishdiscoveries.backend.service.UserService;
import irishdiscoveries.backend.domain.User;
import irishdiscoveries.backend.dto.CreateUserDto;
import irishdiscoveries.backend.dto.LoginUserDto;
import irishdiscoveries.backend.dto.UpdateUserDto;

@RestController
@RequestMapping("/users")
public class UserController extends CrudController<User, CreateUserDto, UpdateUserDto> {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected CrudService<User, CreateUserDto, UpdateUserDto> service() {
        return userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto dto) {
        User user = userService.loginUser(dto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
