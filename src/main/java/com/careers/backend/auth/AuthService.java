package com.careers.backend.auth;

import com.careers.backend.common.exception.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repository,
                       PasswordEncoder encoder,
                       JwtService jwtService) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + email));
    }

    @Transactional
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequest){

        // Check if email already exists
        if (repository.existsByEmail(registerRequest.email())) {
            throw new UserAlreadyExistsException(registerRequest.email());
        }
        User entity = new User(null,
                registerRequest.name(),
                registerRequest.email(),
                encoder.encode(registerRequest.password()),
                registerRequest.role());

        User savedUser = repository.save(entity);

        return new RegisterResponseDto(savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole());




    }


    public LoginResponseDto login(LoginRequestDto loginRequest) {

        String token = jwtService.generateToken(loginRequest.email());
        return new LoginResponseDto(token);
    }



}
