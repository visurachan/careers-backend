package com.careers.backend.auth;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService service;

    @Test
    void shouldRegisterNewUser() throws Exception {
        RegisterResponseDto mockResponse = new RegisterResponseDto(
                1L, "John Smith", "john@test.com", UserRole.CANDIDATE
        );

        when(service.registerUser(any(RegisterRequestDto.class)))
                .thenReturn(mockResponse);

        String requestJson = """
        {
            "name": "John Smith",
            "email": "john@test.com",
            "password": "password123",
            "role": "CANDIDATE"
        }
        """;

        mockMvc.perform(post("/api/auth/registerNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.role").value("CANDIDATE"));
    }
}
