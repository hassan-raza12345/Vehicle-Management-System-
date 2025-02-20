package com.example.Security.Controller;
import com.example.Security.Model.User;
import com.example.Security.Model.VehicleListing;
import com.example.Security.Service.UserService;
import com.example.Security.Service.VehicleListingService;
import com.example.Security.auth.AuthenticationService;
import com.example.Security.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final VehicleListingService vehicleService;
    private final AuthenticationService authenticationService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUserProfile(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();// return object of respons entity with status code 204 and finalize it
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleListing>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllListings());
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> removeVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicleById(id);
        return ResponseEntity.noContent().build();
    }


}
