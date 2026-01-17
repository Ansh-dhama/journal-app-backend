package practicespringboot.dhama.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import practicespringboot.dhama.springboot.Entity.User;
import practicespringboot.dhama.springboot.api.response.DateTimeResponse;
import practicespringboot.dhama.springboot.api.response.WeatherResponse;
import practicespringboot.dhama.springboot.dto.DatetimeDto;
import practicespringboot.dhama.springboot.repo.UserRepo;
import practicespringboot.dhama.springboot.services.DateTimeService;
import practicespringboot.dhama.springboot.services.UserService;
import practicespringboot.dhama.springboot.services.WeatherService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User APIs", description = "Manage user profile and dashboard")
public class Usercontroller { // Ideally rename to UserController (PascalCase)

    private final UserService userService;
    private final UserRepo userRepo;
    private final WeatherService weatherService;
    private final DateTimeService dateTimeService;

    @Operation(summary = "Get Dashboard", description = "Get user weather and time info")
    @GetMapping
    public ResponseEntity<?> getUserDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findByUsername(username);

        // Fail fast if city is missing
        String city = (user.getCity() != null) ? user.getCity() : "Mumbai";

        DateTimeResponse d = dateTimeService.getCurrentTime(city);
        WeatherResponse w = weatherService.getWeather(city);

        // Handle cases where external APIs might fail/return null
        if (d == null || w == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("External services are down");
        }

        DatetimeDto dashboard = new DatetimeDto(
                username,
                d.getDatetime(),
                d.getTimezone_name(),
                d.getTimezone_location(),
                d.getRequested_location(),
                d.getLatitude(),
                d.getLongitude(),
                w.getCurrent() != null ? w.getCurrent().getTempC() : 0.0,
                w.getCurrent() != null ? w.getCurrent().getWindKph() : 0.0
        );

        return ResponseEntity.ok(dashboard);
    }

    @Operation(summary = "Update Profile", description = "Update password or city")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User userFromBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        // Security: Never trust the ID or Username from the body for an update
        User existingUser = userService.findByUsername(loggedInUsername);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        // Only allow updating specific fields
        if (userFromBody.getCity() != null) existingUser.setCity(userFromBody.getCity());
        if (userFromBody.getEmail() != null) existingUser.setEmail(userFromBody.getEmail());
        if (userFromBody.getPassword() != null && !userFromBody.getPassword().isEmpty()) {
            existingUser.setPassword(userFromBody.getPassword()); // Service should handle encoding!
        }

        userService.save(existingUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Account", description = "Permanently delete the user account")
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepo.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}