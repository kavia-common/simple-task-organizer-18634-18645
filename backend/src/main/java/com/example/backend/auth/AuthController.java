package com.example.backend.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Basic authentication endpoints: signup, login, me, logout.
 * Uses session-based security (default in Spring Security).
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Basic user authentication endpoints")
@Validated
public class AuthController {

    public static class SignupRequest {
        @NotBlank @Email public String email;
        @NotBlank @Size(min = 6, max = 100) public String password;
    }

    public static class LoginRequest {
        @NotBlank @Email public String email;
        @NotBlank @Size(min = 6, max = 100) public String password;
    }

    public static class MeResponse {
        public String userId;
        public String email;
    }

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository users, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // PUBLIC_INTERFACE
    @PostMapping("/signup")
    @Operation(summary = "Signup", description = "Create a new user with email and password.")
    public ResponseEntity<?> signup(@RequestBody @Validated SignupRequest req) {
        if (users.findByEmail(req.email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        var user = new User(req.email, passwordEncoder.encode(req.password));
        user = users.save(user);
        var me = new MeResponse();
        me.userId = user.getId();
        me.email = user.getEmail();
        return ResponseEntity.ok(me);
    }

    // PUBLIC_INTERFACE
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with email and password.")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email, req.password)
        );
        // Once authenticated, Spring Security sets the session automatically via filters
        var principal = authentication.getName();
        var user = users.findByEmail(principal).orElseThrow();
        var me = new MeResponse();
        me.userId = user.getId();
        me.email = user.getEmail();
        return ResponseEntity.ok(me);
    }

    // PUBLIC_INTERFACE
    @GetMapping("/me")
    @Operation(summary = "Current user", description = "Returns the authenticated user's id and email.")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).body("Unauthorized");
        var user = users.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");
        var me = new MeResponse();
        me.userId = user.getId();
        me.email = user.getEmail();
        return ResponseEntity.ok(me);
    }

    // PUBLIC_INTERFACE
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out current user and invalidates session.")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.noContent().build();
    }
}
