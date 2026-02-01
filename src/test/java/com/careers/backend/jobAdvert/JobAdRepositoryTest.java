package com.careers.backend.jobAdvert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class JobAdRepositoryTest {

    @Autowired
    private JobAdRepository repository;

    @Test
    void shouldSaveAndFindJobAdById() {
        JobAdvert saved = repository.save(
                new JobAdvert("001","Java Developer")
        );

        Optional<JobAdvert> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Java Developer");

    }
}
