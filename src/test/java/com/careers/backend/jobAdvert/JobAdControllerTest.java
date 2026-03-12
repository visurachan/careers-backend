package com.careers.backend.jobAdvert;

import com.careers.backend.auth.AuthService;
import com.careers.backend.auth.CustomAuthenticationEntryPoint;
import com.careers.backend.common.exception.GlobalExceptionHandler;
import com.careers.backend.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


@WebMvcTest(JobAdController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdGVzdGluZy1vbmx5LXBhZGRpbmc=",
        "jwt.expiration=3600000"
})
class JobAdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobAdService service;

    @MockBean
    private AuthService authService;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Test
    void shouldReturnJob_whenJobExists() throws Exception {
        JobAdvert jobAd = new JobAdvert("001", "Developer");
        when(service.getJobAdById("001")).thenReturn(jobAd);

        mockMvc.perform(get("/api/jobAds/001")
                        .with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Developer"));
    }

    @Test
    void shouldReturn404_whenJobNotFound() throws Exception {
        when(service.getJobAdById("99")).thenThrow(new JobAdNotFoundException("99"));

        mockMvc.perform(get("/api/jobAds/99")
                        .with(anonymous()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllJobs() throws Exception {
        JobAdvert jobAd1 = new JobAdvert("job1", "Java Developer");
        JobAdvert jobAd2 = new JobAdvert("jobAd2", "Python Developer");
        Page<JobAdvert> jobPage = new PageImpl<>(
                Arrays.asList(jobAd1, jobAd2),
                PageRequest.of(0, 10),
                2);

        when(service.getAllJobAds(0, 10)).thenReturn(jobPage);

        mockMvc.perform(get("/api/jobAds?page=0&size=10")
                        .with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value("Java Developer"))
                .andExpect(jsonPath("$.content[1].title").value("Python Developer"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.pageSize").value(10));
    }

    @Test
    void shouldCreateNewJob_whenDataProvided() throws Exception {
        JobAdDtoAllFields newJob = new JobAdDtoAllFields("xx1", "Civil Engineer", null, null, null, null, JobAdStatus.LIVE, "test@test.com");

        when(service.createNewJob(any(JobAdDTO.class), eq("test@test.com"))).thenReturn(newJob);

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
                        .content(jobJson)
                        .with(jwt().jwt(j -> j.subject("test@test.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_COMPANY"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("xx1"))
                .andExpect(jsonPath("$.title").value("Civil Engineer"))
                .andExpect(jsonPath("$.jobAdStatus").value("LIVE"))
                .andExpect(jsonPath("$.postedBy").value("test@test.com"));
    }
    @Test
    void shouldReturn403_whenCandidateTriesToPostJobAd() throws Exception {
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
                        .content(jobJson)
                        .with(jwt().jwt(j -> j.subject("candidate@test.com")
                                .claim("role", "CANDIDATE"))))
                .andExpect(status().isForbidden());
    }
}