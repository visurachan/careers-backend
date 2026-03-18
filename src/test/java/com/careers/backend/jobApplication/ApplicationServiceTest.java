package com.careers.backend.jobApplication;


import com.careers.backend.auth.User;
import com.careers.backend.auth.UserRepository;
import com.careers.backend.auth.UserRole;
import com.careers.backend.common.exception.DuplicateApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    ApplicationRepository repository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ApplicationService service;

    @Test
    void shouldSubmitJobApplication(){
        ApplicationRequestDto requestDto = new ApplicationRequestDto("I am interested in this job");

        User candidate = new User(1L, "John Candidate", "candidate@test.com", "hashedPassword", UserRole.CANDIDATE);
        when(userRepository.findByEmail("candidate@test.com")).thenReturn(Optional.of(candidate));
        JobApplication savedApplication = new JobApplication(
                1L,
                "job1",
                "candidate@test.com",
                "John Candidate",
                "I am interested in this job",
                LocalDateTime.now(),
                ApplicationStatus.SUBMITTED
        );
        when(repository.save(any())).thenReturn(savedApplication);

        ApplicationResponseDto result = service.applyForJob("job1","candidate@test.com",requestDto);
        assertThat(result.jobAdId()).isEqualTo("job1");
        assertThat(result.candidateEmail()).isEqualTo("candidate@test.com");
        assertThat(result.candidateName()).isEqualTo("John Candidate");
        assertThat(result.coverNote()).isEqualTo("I am interested in this job");
        assertThat(result.status()).isEqualTo(ApplicationStatus.SUBMITTED);
        verify(repository).save(any());
    }

    @Test
    void shouldThrowException_whenCandidateAppliesForSameJobTwice() {
        ApplicationRequestDto request = new ApplicationRequestDto("I am very interested in this role.");

        User candidate = new User(1L, "John Candidate", "candidate@test.com", "hashedPassword", UserRole.CANDIDATE);
        when(userRepository.findByEmail("candidate@test.com")).thenReturn(Optional.of(candidate));
        when(repository.existsByJobAdIdAndCandidateEmail("job-001", "candidate@test.com")).thenReturn(true);

        assertThatThrownBy(() -> service.applyForJob("job-001", "candidate@test.com", request))
                .isInstanceOf(DuplicateApplicationException.class);
    }



}
