package com.example.Security.Controller;
import com.example.Security.Model.User;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;





    @GetMapping
    public ResponseEntity<User> getCurrentUserProfile() {
        User user = userService.getCurrentUserProfile();
        return ResponseEntity.ok(user);
    }
    @PutMapping
    public ResponseEntity<User> updateCurrentUserProfile(@RequestBody User updatedUser) {
        User user = userService.updateCurrentUserProfile(updatedUser);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }


}
