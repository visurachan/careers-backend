package com.careers.backend.jobAdvert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Test
    void shouldFindJobAdsByPostedBy() {
        repository.save(new JobAdvert("job-001", "Java Developer", "5+ years", "London", LocalDate.of(2026, 12, 31), LocalDateTime.now(), JobAdStatus.LIVE, "company@test.com"));
        repository.save(new JobAdvert("job-002", "Python Developer", "3+ years", "Manchester", LocalDate.of(2026, 12, 31), LocalDateTime.now(), JobAdStatus.LIVE, "company@test.com"));
        repository.save(new JobAdvert("job-003", "Frontend Developer", "React experience", "London", LocalDate.of(2026, 12, 31), LocalDateTime.now(), JobAdStatus.LIVE, "other@test.com"));

        Page<JobAdvert> result = repository.findByPostedBy("company@test.com", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).allMatch(job -> job.getPostedBy().equals("company@test.com"));
    }
}
