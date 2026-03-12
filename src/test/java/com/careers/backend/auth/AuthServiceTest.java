package com.careers.backend.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository repository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService service;

    @Test
    void shouldRegisterNewUserWithHashedPassword(){
        RegisterRequestDto registerRequest = new RegisterRequestDto("John Smith","jsmith@gmail.com","password123",UserRole.CANDIDATE);
        User savedUser = new User(1L,"John Smith","jsmith@gmail.com","hashedPassword",UserRole.CANDIDATE);

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        when(repository.save(any())).thenReturn(savedUser);

        RegisterResponseDto registerResponse = service.registerUser(registerRequest);

        assertThat(registerResponse.email()).isEqualTo("jsmith@gmail.com");
        assertThat(registerResponse.role()).isEqualTo(UserRole.CANDIDATE);
        verify(passwordEncoder).encode("password123");
        verify(repository).save(any());

    }

    @Test
    void shouldLoginAndReturnToken(){
        LoginRequestDto request = new LoginRequestDto(
                "john@test.com","password123"
        );

        User user = new User(1L, "John", "john@test.com", "hashedPassword", UserRole.CANDIDATE);

        when(repository.findByEmail("john@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("john@test.com", "CANDIDATE")).thenReturn("mocked.jwt.token");

        LoginResponseDto result = service.login(request);
        assertThat(result.token()).isEqualTo("mocked.jwt.token");

        verify(jwtService).generateToken("john@test.com", "CANDIDATE");
    }


}
