package com.careers.backend.jobAdvert;

import com.careers.backend.common.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class JobAdControllerTest {


    private MockMvc mockMvc;

    @Mock
    JobAdService service;

    @InjectMocks
    private JobAdController controller;

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




}
