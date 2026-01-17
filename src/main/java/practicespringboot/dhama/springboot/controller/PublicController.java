package practicespringboot.dhama.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import practicespringboot.dhama.springboot.Entity.User;
import practicespringboot.dhama.springboot.dto.UserDto;
import practicespringboot.dhama.springboot.services.UserService;
import practicespringboot.dhama.springboot.utiles.JwtUtilie;


import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtilie jwtUtilies;
    private final UserService service;

    public PublicController(AuthenticationManager authenticationManager,
                            JwtUtilie jwtUtilies,
                            UserService service) {
        this.authenticationManager = authenticationManager;
        this.jwtUtilies = jwtUtilies;
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {

        // 1. Use the DTO for checks
        if (service.existsByUsername(userDto.getUsername())) {
            log.warn("Username already taken: {}", userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        if (service.existsByEmail(userDto.getEmail())) {
            log.warn("Email already taken: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken");
        }

        // 2. Convert DTO to Entity
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(userDto.getPassword());
        // Note: Your service.create() method handles the password encryption, so passing plain text here is fine.

        // 3. Save
        service.create(newUser);

        // 4. Return Success (Without leaking the User object)
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            String username = auth.getName(); // authenticated username
            String token = jwtUtilies.generateToken(username);

            // return token as JSON
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "type", "Bearer",
                    "username", username
            ));

        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Username or password incorrect");
        }
    }
}
