package com.careers.backend.jobAdvert;

import com.careers.backend.common.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class JobAdControllerTest {


    private MockMvc mockMvc;

    @Mock
    JobAdService service;

    @InjectMocks
    private JobAdController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnJob_whenJobExists() throws Exception {
        JobAdvert jobAd = new JobAdvert("001","Developer");
        when(service.getJobAdById("001")).thenReturn(jobAd);

        mockMvc.perform(get("/api/jobAds/001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Developer"));
    }

    @Test
    void shouldReturn404_whenJobNotFound() throws Exception {
        when(service.getJobAdById("99")).thenThrow(new JobAdNotFoundException("99"));

        mockMvc.perform(get("/api/jobAds/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllJobs() throws Exception {
        JobAdvert jobAd1 = new JobAdvert("job1","Java Developer");
        JobAdvert jobAd2 =  new JobAdvert("jobAd2","Python Developer");
        List<JobAdvert> jobList = Arrays.asList(jobAd1,jobAd2);

        when(service.getAllJobAds()).thenReturn(jobList);

        mockMvc.perform(get("/api/jobAds"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Java Developer"))
                .andExpect(jsonPath("$[1].title").value("Python Developer"));
    }

    @Test
    void shouldCreateNewJob_whenDataProvided() throws Exception {
        JobAdDtoAllFields newJob = new JobAdDtoAllFields("xx1","Civil Engineer",null, null,null,null,JobAdStatus.LIVE);

        when(service.createNewJob(any(JobAdDTO.class))).thenReturn(newJob);
        String jobJson = """
                {
                             "id": "xx1",
                             "title": "Civil Engineer",
                             "description": "5+ years experience",
                             "location": "Colombo",
                             "expiryDate": "2026-12-31"
                         }
                """;
        mockMvc.perform(post("/api/jobAds")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jobJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("xx1"))
                .andExpect(jsonPath("$.title").value("Civil Engineer"))
                .andExpect(jsonPath("$.jobAdStatus").value("LIVE"));


    }







}


