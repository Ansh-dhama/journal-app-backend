package practicespringboot.dhama.springboot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicespringboot.dhama.springboot.Entity.User;
import practicespringboot.dhama.springboot.repo.UserRepo;
import practicespringboot.dhama.springboot.repo.UserRepoImp;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private EmailService emailService;

    private final UserRepo users;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepoImp userRepoImp;

    public User create(User newUser) {
        newUser.setId(null);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if(newUser.getEmail() == null || newUser.getEmail().isEmpty()){
            throw new RuntimeException("Email is empty");
        }
        try {
            users.save(newUser);
            emailService.sendEmail(newUser.getEmail(), "Welcome to springboot app " ,"Hi " + newUser.getUsername() + " your account is created!");
            return newUser;
        } catch (DuplicateKeyException e) {   // Spring wraps Mongo duplicate key
            // This will now trigger if either the username OR email already exists
            throw new DuplicateKeyException("User with this username or email already exists");
        }
    }

    public User save(User existingUser) {
        return users.save(existingUser);   // no re-encoding, no reset of journalEntries
    }
    public boolean existsByUsername(String username) {
        return users.existsByUsername(username);
    }
    public boolean existsByEmail(String email) {
        return users.existsByEmail(email);
    }
    public User Update(User updatedUser) {
        if (updatedUser.getUsername() == null || updatedUser.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank");
        }

        User existingUser = Optional.ofNullable(users.findByUsername(updatedUser.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        // Replace roles completely
        if (updatedUser.getRoles() != null) {
            if (existingUser.getRoles() == null) {
                existingUser.setRoles(new java.util.ArrayList<>());
            }
            existingUser.getRoles().clear();
            existingUser.getRoles().addAll(updatedUser.getRoles());
        }
        if(updatedUser.getEmail()!=null && !updatedUser.getEmail().isEmpty()){
            existingUser.setEmail(updatedUser.getEmail());
        }

        return save(existingUser);
    }



    public List<User> getAll() {
        return users.findAll();
    }
    public List<User> getAllCreateria(){
        return userRepoImp.findAll();
    }

    public Optional<User> getById(String id) {
        try { return users.findById(new ObjectId(id)); }
        catch (IllegalArgumentException e) { return Optional.empty(); }
    }

    public Optional<User> delete(ObjectId oid) {
        try {

            return users.findById(oid).map(u -> { users.deleteById(oid); return u; });
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public User findByUsername(String username) {
        return users.findByUsername(username);
    }
    @Transactional
    public User saveAdmin(User user) {
        // Make sure it's treated as a new user
        user.setId(null);

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Ensure roles list exists
        List<String> roles = user.getRoles();
        if (roles == null) {
            roles = new ArrayList<>();
        }

        // Ensure ADMIN is present
        if (roles.stream().noneMatch("ADMIN"::equals)) {
            roles.add("ADMIN");
        }
        user.setRoles(roles);

        try {
            return users.save(user);
        } catch (DuplicateKeyException e) {
            System.out.println("duplicate key exception");

            throw new IllegalArgumentException("Username already taken");
        }
    }

    }

