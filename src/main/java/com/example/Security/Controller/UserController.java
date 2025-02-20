package com.example.Security.Controller;
import com.example.Security.Model.User;
import com.example.Security.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class UserController
{
    //@autowaired
    private UserService userService;

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


}
