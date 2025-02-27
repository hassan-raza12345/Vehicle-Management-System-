package com.example.Security.Service;
import com.example.Security.Model.User;
import com.example.Security.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private Optional<User> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email);
    }

    public User getCurrentUserProfile() {
        return getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User updateCurrentUserProfile(User updatedUser)
    {
        User currentUser = getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getFirstName() != null)
        {
            currentUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null)
        {
            currentUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null)
        {
            currentUser.setEmail(updatedUser.getEmail());
        }

        return userRepository.save(currentUser);
    }
    public User updateUserProfile(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        return userRepository.save(existingUser);
    }

    public List<User> getAllUsers()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() == null || !currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Access denied: Only admins can view all users.");
        }

        return userRepository.findAll();
    }
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

}
