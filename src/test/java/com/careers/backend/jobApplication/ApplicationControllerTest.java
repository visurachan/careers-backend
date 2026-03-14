package com.careers.backend.jobApplication;

import com.careers.backend.auth.AuthService;
import com.careers.backend.auth.CustomAccessDeniedHandler;
import com.careers.backend.auth.CustomAuthenticationEntryPoint;
import com.careers.backend.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@TestPropertySource(properties = {
        "jwt.secret=dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdGVzdGluZy1vbmx5LXBhZGRpbmc=",
        "jwt.expiration=3600000"
})
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService service;

    @MockBean
    private AuthService authService;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    void shouldSubmitJobApplication() throws Exception {
        ApplicationResponseDto responseDto = new ApplicationResponseDto(
                1L,
                "job-001",
                "candidate@test.com",
                "John Candidate",
                "I am very interested in this role.",
                LocalDateTime.now(),
                ApplicationStatus.SUBMITTED
        );

        when(service.applyForJob(eq("job-001"), eq("candidate@test.com"), any(ApplicationRequestDto.class)))
                .thenReturn(responseDto);

        String applyJson = """
                {"coverNote":"I am very interested in this role."}
                """;

        mockMvc.perform(post("/api/jobAds/job-001/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applyJson)
                        .with(jwt().jwt(j -> j.subject("candidate@test.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_CANDIDATE"))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobAdId").value("job-001"))
                .andExpect(jsonPath("$.candidateEmail").value("candidate@test.com"))
                .andExpect(jsonPath("$.candidateName").value("John Candidate"))
                .andExpect(jsonPath("$.coverNote").value("I am very interested in this role."))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }
}
