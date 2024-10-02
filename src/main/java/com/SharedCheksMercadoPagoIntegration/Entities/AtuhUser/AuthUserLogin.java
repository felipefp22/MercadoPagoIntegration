package com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser;

import com.SharedCheksMercadoPagoIntegration.Entities.AtuhUser.AuthUserDTOs.RegisterAuthUserDTO;
import com.SharedCheksMercadoPagoIntegration.Infra.auth.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "auth_user_mp_pauments")
public class AuthUserLogin implements UserDetails {
    @Id
    private String username;
    private String password;
    private Role role;
    private LocalDateTime createdAtUTC;

    // <>------------ Constructors ------------<>

    public AuthUserLogin() {
    }

    public AuthUserLogin(String username, String password) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.role = Role.USER;
        this.createdAtUTC = LocalDateTime.now(ZoneOffset.UTC);
    }

    public AuthUserLogin(RegisterAuthUserDTO registerAuthUserDTO) {
        this.username = registerAuthUserDTO.username();
        this.password = new BCryptPasswordEncoder().encode(registerAuthUserDTO.password());
        this.role = Role.USER;
        this.createdAtUTC = LocalDateTime.now(ZoneOffset.UTC);

    }

    // <>------------ Getters and setters ------------<>
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));

        if (this.role == Role.USER) return List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public LocalDateTime getCreatedAtUTC() {
        return createdAtUTC;
    }


}
