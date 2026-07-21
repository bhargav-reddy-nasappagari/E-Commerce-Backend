package com.ecommerce.backend.service.impl;

import com.ecommerce.backend.dto.request.LoginRequest;
import com.ecommerce.backend.dto.request.RegisterRequest;
import com.ecommerce.backend.dto.response.AuthResponse;
import com.ecommerce.backend.entity.Role;
import com.ecommerce.backend.entity.RoleName;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.exception.DuplicateResourceException;
import com.ecommerce.backend.repository.RoleRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.security.JwtUtil;
import com.ecommerce.backend.security.UserDetailsImpl;
import com.ecommerce.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "A user with email '" + email + "' already exists."
            );
        }

        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException(
                        "Required role CUSTOMER is not present in the database. " +
                        "Ensure roles are seeded during application startup."
                ));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(customerRole);

        User savedUser = userRepository.save(user);

        UserDetailsImpl principal = UserDetailsImpl.build(savedUser);

        String token = jwtUtil.generateToken(principal);

        return buildAuthResponse(principal, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email = normalizeEmail(request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        UserDetailsImpl principal =
                (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtil.generateToken(principal);

        return buildAuthResponse(principal, token);
    }

    private AuthResponse buildAuthResponse(
            UserDetailsImpl principal,
            String token
    ) {

        return AuthResponse.builder()
                .token(token)
                .email(principal.getUsername())
                .fullName(principal.getFullName())
                .roles(
                        principal.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}