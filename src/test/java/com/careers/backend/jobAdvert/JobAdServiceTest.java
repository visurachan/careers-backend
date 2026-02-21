package com.careers.backend.jobAdvert;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void shouldReturnAllJobAds(){
        JobAdvert jobAd1 = new JobAdvert("job1","Java Developer");
        JobAdvert jobAd2 = new JobAdvert("job2","Python Developer");
        List<JobAdvert> jobList = Arrays.asList(jobAd1,jobAd2);

        when(repository.findAll()).thenReturn(jobList);

        List<JobAdvert> result = service.getAllJobAds();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Developer");
        assertThat(result.get(1).getTitle()).isEqualTo("Python Developer");


    }

    @Test
    void shouldCreateNewJobAd_withLiveStatusPostedDateTime(){
        // Arrange
        JobAdDTO request = new JobAdDTO(
                "101",
                "Senior Java Developer",
                "5+ years experience",
                "London",
                LocalDate.of(2026, 12, 31)
        );

        JobAdvert savedEntity = new JobAdvert(
                "101",
                "Senior Java Developer",
                "5+ years experience",
                "London",
                LocalDate.of(2026, 12, 31),
                LocalDateTime.now(),
                JobAdStatus.LIVE
        );

        when(repository.<JobAdvert>save(any())).thenReturn(savedEntity);

        // Act
        JobAdDtoAllFields result = service.createNewJob(request);

        // Assert
        assertThat(result.id()).isEqualTo("101");
        assertThat(result.title()).isEqualTo("Senior Java Developer");
        assertThat(result.jobAdStatus()).isEqualTo(JobAdStatus.LIVE);
        assertThat(result.postedDateTime()).isNotNull();
        verify(repository).save(ArgumentMatchers.<JobAdvert>any());
    }


}







