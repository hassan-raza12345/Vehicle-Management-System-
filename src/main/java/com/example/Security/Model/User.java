package com.example.Security.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private String password;

    @Column(unique = true, nullable = false)
    private String email;
     @Lob
     private String profilePicture;
    @Enumerated(EnumType.STRING) //
    private Role role;

    @OneToMany(mappedBy = "seller")
    @JsonIgnore
    private List<VehicleListing> vehiclesForSale;

    @OneToMany(mappedBy = "buyer")
    @JsonIgnore
    private List<VehicleListing> vehiclesPurchased;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Token> tokens;
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null) {
            throw new IllegalStateException("User role is null!");
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
