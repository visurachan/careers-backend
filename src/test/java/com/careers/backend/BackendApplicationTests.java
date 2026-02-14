package com.careers.backend;

import com.careers.backend.jobAdvert.JobAdvert;
import com.careers.backend.jobAdvert.JobAdRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobAdRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        JobAdvert testJob = new JobAdvert("99", "Test Developer Position");
        repository.save(testJob);
    }

    @Test
    void shouldReturnAJobAdvertWhenDataExists() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity("/api/jobAds/99", String.class);  // Changed URL!

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("99");
    }

    @Test
    void shouldNotReturnAJobAdvertWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity("/api/jobAds/1000", String.class);  // Changed URL!

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}