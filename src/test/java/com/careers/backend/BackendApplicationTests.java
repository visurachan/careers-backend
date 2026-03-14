package com.careers.backend;

import com.careers.backend.auth.UserRepository;
import com.careers.backend.jobAdvert.JobAdStatus;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobAdRepository repository;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        userRepository.deleteAll();

        JobAdvert testJob = new JobAdvert("99", "Test Developer Position");
        repository.save(testJob);

        token = getToken();
    }

    private String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String registerJson = """
            {"name":"Test User","email":"test@test.com","password":"password123","role":"COMPANY"}
            """;
        restTemplate.postForEntity("/api/auth/registerNewUser",
                new HttpEntity<>(registerJson, headers), String.class);

        String loginJson = """
            {"email":"test@test.com","password":"password123"}
            """;
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/login", new HttpEntity<>(loginJson, headers), String.class);

        return JsonPath.parse(response.getBody()).read("$.token");
    }

    private HttpEntity<Void> authRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<String> authRequest(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    @Test
    void shouldReturnAJobAdvertWhenDataExists() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/jobAds/99",String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String id = JsonPath.parse(response.getBody()).read("$.id");
        assertThat(id).isEqualTo("99");
    }

    @Test
    void shouldNotReturnAJobAdvertWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/jobAds/1000", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnAllJobAdverts() {
        repository.save(new JobAdvert("100", "Python Developer"));

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/jobAds?page=0&size=10",String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext doc = JsonPath.parse(response.getBody());
        assertThat((int) doc.read("$.totalElements")).isEqualTo(2);
        assertThat((String) doc.read("$.content[0].id")).isEqualTo("99");
        assertThat((String) doc.read("$.content[1].id")).isEqualTo("100");
    }

    @Test
    void shouldReturnCorrectPageSize() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/jobAds?page=0&size=1",String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext doc = JsonPath.parse(response.getBody());
        assertThat((int) doc.read("$.pageSize")).isEqualTo(1);
        assertThat((int) doc.read("$.content.length()")).isEqualTo(1);
    }

    @Test
    void shouldCreateNewJobAdvert() {
        String jobJson = """
        {
            "id": "200",
            "title": "Civil Engineer",
            "description": "Building Construction expert needed",
            "location": "Colombo",
            "expiryDate": "2026-06-30"
        }
        """;

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/jobAds", HttpMethod.POST, authRequest(jobJson), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        DocumentContext doc = JsonPath.parse(response.getBody());
        assertThat((String) doc.read("$.id")).isEqualTo("200");
        assertThat((String) doc.read("$.title")).isEqualTo("Civil Engineer");
        assertThat((String) doc.read("$.postedBy")).isEqualTo("test@test.com");
    }

    @Test
    void shouldRegisterNewUser() {
        String userJson = """
        {
            "name": "John Smith",
            "email": "john@test.com",
            "password": "password123",
            "role": "CANDIDATE"
        }
        """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/registerNewUser", new HttpEntity<>(userJson, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        DocumentContext doc = JsonPath.parse(response.getBody());
        assertThat((String) doc.read("$.email")).isEqualTo("john@test.com");
        assertThat((String) doc.read("$.role")).isEqualTo("CANDIDATE");
    }

    @Test
    void shouldLoginAndReturnJwtToken() {
        // user already registered in setUp() via getToken()
        String loginJson = """
            {"email":"test@test.com","password":"password123"}
            """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/auth/login", new HttpEntity<>(loginJson, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String token = JsonPath.parse(response.getBody()).read("$.token");
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void shouldReturn403_whenCandidateTriesToPostJobAd() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String registerJson = """
        {"name":"Candidate User","email":"candidate@test.com","password":"password123","role":"CANDIDATE"}
        """;
        restTemplate.postForEntity("/api/auth/registerNewUser",
                new HttpEntity<>(registerJson, headers), String.class);

        String loginJson = """
        {"email":"candidate@test.com","password":"password123"}
        """;
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/api/auth/login", new HttpEntity<>(loginJson, headers), String.class);
        String candidateToken = JsonPath.parse(loginResponse.getBody()).read("$.token");

        String jobJson = """
        {"id":"999","title":"Test Job","description":"Test","location":"Test","expiryDate":"2026-12-31"}
        """;
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(candidateToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/jobAds", HttpMethod.POST,
                new HttpEntity<>(jobJson, authHeaders), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }



    @Test
    void shouldReturnJobAdsByCompany() {
        repository.deleteAll();

        JobAdvert job1 = new JobAdvert("job-001", "Senior Java Developer", "5+ years experience", "London", LocalDate.of(2026, 12, 31), LocalDateTime.now(), JobAdStatus.LIVE, "company@test.com");
        JobAdvert job2 = new JobAdvert("job-002", "Python Developer", "3+ years experience", "Manchester", LocalDate.of(2026, 12, 31), LocalDateTime.now(), JobAdStatus.LIVE, "company@test.com");
        JobAdvert job3 = new JobAdvert("job-003", "Frontend Developer", "React experience", "London", LocalDate.of(2026, 12, 31), LocalDateTime.now(), JobAdStatus.LIVE, "other@test.com");

        repository.saveAll(Arrays.asList(job1, job2, job3));

        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/jobAds?postedBy=company@test.com&page=0&size=10", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext doc = JsonPath.parse(response.getBody());
        assertThat((int) doc.read("$.totalElements")).isEqualTo(2);
        assertThat((String) doc.read("$.content[0].postedBy")).isEqualTo("company@test.com");
        assertThat((String) doc.read("$.content[1].postedBy")).isEqualTo("company@test.com");
    }

    @Test
    void shouldSubmitJobApplication(){
        repository.deleteAll();
        JobAdvert jobAd = new JobAdvert("job1","Carpenter","Kitchen Speciality","Sydney",LocalDate.of(2026,06,30),LocalDateTime.now(),JobAdStatus.LIVE,"company@test.com");
        repository.save(jobAd);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String registerJson = """
            {   
            "name": "John Candidate",
            "email": "j.candidate@test.com",
            "password": "password456",
            "role": "CANDIDATE"
            }
                """;
        restTemplate.postForEntity("/api/auth/registerNewUser",
                new HttpEntity<>(registerJson, headers), String.class);

        String loginJson = """
                {"email":"j.candidate@test.com","password":"password456"}
                """;

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "/api/auth/login", new HttpEntity<>(loginJson,headers), String.class);

        String candidateToken = JsonPath.parse(loginResponse.getBody()).read("$.token");

        String applyJson = """
                {"coverNote":"I am very interested in this role."}
                """;
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.setBearerAuth(candidateToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/jobAds/job1/apply", HttpMethod.POST,
                new HttpEntity<>(applyJson,authHeaders), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        DocumentContext doc = JsonPath.parse(response.getBody());
        assertThat((String) doc.read("$.jobAdId")).isEqualTo("job1");
        assertThat((String) doc.read("$.candidateEmail")).isEqualTo("j.candidate@test.com");
        assertThat((String) doc.read("$.candidateName")).isEqualTo("John Candidate");
        assertThat((String) doc.read("$.coverNote")).isEqualTo("I am very interested in this role.");
        assertThat((String) doc.read("$.appliedAt")).isNotNull();

    }
}
