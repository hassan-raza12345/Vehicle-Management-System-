package com.example.Security.Controller;
import com.example.Security.Model.User;
import com.example.Security.Repository.UserRepository;
import com.example.Security.Service.ReviewService;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;
    private final ReviewService reviewService;





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


    // 📊 Get average rating for a user as a seller
    @GetMapping("/{id}/sellerrating")
    public ResponseEntity<Double> getSellerRating(@PathVariable Long id ){
        System.out.println("Fetching seller rating for user ID: " + id);
        double sellerRating = reviewService.getAverageRatingAsSeller(id);
        return ResponseEntity.ok(sellerRating);
    }

}
