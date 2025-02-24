
package com.example.Security.auth;
import com.example.Security.Model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestParam("firstname") String firstName,
            @RequestParam("lastname") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") Role role,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        System.out.println("Received password: " + password);
        RegisterRequest request = new RegisterRequest(firstName, lastName, email, password, role, profilePicture);
        System.out.println("DTO password: " + request.getPassword());
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }





}
