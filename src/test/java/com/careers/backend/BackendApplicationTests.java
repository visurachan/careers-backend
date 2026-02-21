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
import org.springframework.http.*;

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

    @Test
    void shouldReturnAllJobAdverts() {

        JobAdvert job2 = new JobAdvert("100", "Python Developer");
        repository.save(job2);


        ResponseEntity<String> response = restTemplate
                .withBasicAuth("test", "test")
                .getForEntity("/api/jobAds", String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int jobCount = documentContext.read("$.length()");
        assertThat(jobCount).isEqualTo(2);

        // Verify first job
        String id1 = documentContext.read("$[0].id");
        assertThat(id1).isEqualTo("99");

        // Verify second job
        String id2 = documentContext.read("$[1].id");
        assertThat(id2).isEqualTo("100");
    }

    @Test
    void shouldCreateNewJobAdvert() {
        // Arrange
        String jobJson = """
        {
            "id": "200",
            "title": "Civil Engineer",
            "description": "Building Construction expert needed",
            "location": "Colombo",
            "expiryDate": "2026-06-30"
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jobJson, headers);

        // Act
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("test", "test")
                .postForEntity("/api/jobAds", request, String.class);

        System.out.println(response.getBody());
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        String title = documentContext.read("$.title");

        assertThat(id).isEqualTo("200");
        assertThat(title).isEqualTo("Civil Engineer");
    }
}