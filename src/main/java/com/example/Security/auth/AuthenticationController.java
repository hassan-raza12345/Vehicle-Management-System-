
package com.example.Security.auth;
import com.example.Security.Model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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

        RegisterRequest request = new RegisterRequest(firstName, lastName, email, password, role, profilePicture);
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticates(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse authResponse = service.authenticate(request);

            ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", authResponse.getAccessToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(3600)
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", jwtCookie.toString())
                    .body(Map.of("message", "Authentication successful"));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed: " + ex.getMessage()));
        }
    }




}
