package com.careers.backend.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    void shouldFindUserByEmail() {
        User user = new User(
                null,
                "John Smith",
                "john@test.com",
                "hashedpassword",
                UserRole.CANDIDATE
        );
        repository.save(user);

        Optional<User> found = repository.findByEmail("john@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@test.com");
        assertThat(found.get().getName()).isEqualTo("John Smith");
    }

    @Test
    void shouldReturnEmpty_whenEmailNotFound() {
        Optional<User> found = repository.findByEmail("notexist@test.com");

        assertThat(found).isEmpty();
    }
}
