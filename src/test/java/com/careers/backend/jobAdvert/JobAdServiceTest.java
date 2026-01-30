package com.careers.backend.jobAdvert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobAdServiceTest {

    @Mock
    JobAdRepository repository;

    @InjectMocks
    JobAdService service;

    @Test
    void shouldReturnJob_whenJobAdExists(){
        JobAdvert jobAd = new JobAdvert("012","Developer");
        when(repository.findById("012")).thenReturn(Optional.of(jobAd));

        JobAdvert result = service.getJobAdById("012");

        assertThat(result.getTitle()).isEqualTo("Developer");
    }

    @Test
    void shouldThrowException_whenJobDoesNotExist() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getJobAdById("99"))
                .isInstanceOf(JobAdNotFoundException.class);
    }
}


