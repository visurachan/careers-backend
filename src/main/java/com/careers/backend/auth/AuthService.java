package com.careers.backend.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public RegisterResponseDto registerUser(RegisterRequestDto registerRequest){
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

}
