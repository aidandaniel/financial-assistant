package com.finsight.controller;

import com.finsight.model.*;
import com.finsight.service.UserService;
import com.finsight.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for user operations
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations for user management and profiles")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User user = userService.registerUser(registrationDTO);
            return ResponseEntity.ok(new UserResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering user: " + e.getMessage());
        }
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            var authResponse = userService.authenticateUser(loginDTO.getEmail(), loginDTO.getPassword());
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error authenticating user: " + e.getMessage());
        }
    }
    
    @GetMapping("/{userId}")
    @Operation(summary = "Get user profile", description = "Retrieves user profile information")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                return ResponseEntity.ok(new UserResponseDTO(user.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user profile: " + e.getMessage());
        }
    }
    
    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile", description = "Updates user profile information")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody UserUpdateDTO updateDTO) {
        try {
            User user = userService.updateUserProfile(userId, updateDTO);
            return ResponseEntity.ok(new UserResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user profile: " + e.getMessage());
        }
    }
    
    @PostMapping("/{userId}/goals")
    @Operation(summary = "Add user goal", description = "Adds a new financial goal for the user")
    public ResponseEntity<?> addUserGoal(@PathVariable Long userId, @RequestBody UserGoalDTO goalDTO) {
        try {
            UserGoal userGoal = userService.addUserGoal(userId, goalDTO);
            return ResponseEntity.ok(userGoal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding user goal: " + e.getMessage());
        }
    }
    
    @GetMapping("/{userId}/goals")
    @Operation(summary = "Get user goals", description = "Retrieves all user's financial goals")
    public ResponseEntity<?> getUserGoals(@PathVariable Long userId) {
        try {
            List<UserGoal> goals = userService.getUserGoals(userId);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user goals: " + e.getMessage());
        }
    }
    
    @PutMapping("/{userId}/knowledge-level")
    @Operation(summary = "Update knowledge level", description = "Updates user's financial knowledge level")
    public ResponseEntity<?> updateKnowledgeLevel(@PathVariable Long userId, @RequestBody KnowledgeLevelDTO levelDTO) {
        try {
            User user = userService.updateKnowledgeLevel(userId, levelDTO.getLevel());
            return ResponseEntity.ok(new UserResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating knowledge level: " + e.getMessage());
        }
    }
}

// DTOs
class UserRegistrationDTO {
    private String email;
    private String password;
    private String name;
    private Integer age;
    private String postalCode;
    private String phone;
    private String primaryBank;
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPrimaryBank() { return primaryBank; }
    public void setPrimaryBank(String primaryBank) { this.primaryBank = primaryBank; }
}

class LoginDTO {
    private String email;
    private String password;
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class UserUpdateDTO {
    private String name;
    private Integer age;
    private String postalCode;
    private String phone;
    private String primaryBank;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPrimaryBank() { return primaryBank; }
    public void setPrimaryBank(String primaryBank) { this.primaryBank = primaryBank; }
}

class UserResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String username;
    private Integer age;
    private String postalCode;
    private String phone;
    private String primaryBank;
    private KnowledgeLevel knowledgeLevel;
    
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.username = user.getUsername();
        this.age = user.getAge();
        this.postalCode = user.getPostalCode();
        this.phone = user.getPhone();
        this.primaryBank = user.getPrimaryBank();
        this.knowledgeLevel = user.getKnowledgeLevel();
    }
    
    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public Integer getAge() { return age; }
    public String getPostalCode() { return postalCode; }
    public String getPhone() { return phone; }
    public String getPrimaryBank() { return primaryBank; }
    public KnowledgeLevel getKnowledgeLevel() { return knowledgeLevel; }
}

class UserGoalDTO {
    private String goalName;
    private String customGoalName;
    private Double targetAmount;
    private String targetDate;
    private Boolean isPrimary;
    
    // Getters and Setters
    public String getGoalName() { return goalName; }
    public void setGoalName(String goalName) { this.goalName = goalName; }
    
    public String getCustomGoalName() { return customGoalName; }
    public void setCustomGoalName(String customGoalName) { this.customGoalName = customGoalName; }
    
    public Double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(Double targetAmount) { this.targetAmount = targetAmount; }
    
    public String getTargetDate() { return targetDate; }
    public void setTargetDate(String targetDate) { this.targetDate = targetDate; }
    
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
}

class KnowledgeLevelDTO {
    private KnowledgeLevel level;
    
    // Getters and Setters
    public KnowledgeLevel getLevel() { return level; }
    public void setLevel(KnowledgeLevel level) { this.level = level; }
}
