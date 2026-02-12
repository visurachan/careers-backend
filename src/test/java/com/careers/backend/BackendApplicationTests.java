package com.careers.backend;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class BackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;  // Now autowiring works correctly

    @Test
    void shouldReturnAJobAdvertWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("test","test").getForEntity("/jobads/99", String.class);
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("99");
    }

    @Test
    void shouldNotReturnAJobAdvertWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("test","test").getForEntity("/jobads/1000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(response.getBody());
    }

}

