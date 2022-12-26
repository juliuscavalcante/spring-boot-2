package com.spring.springboot2.integration;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.domain.SpringUser;
import com.spring.springboot2.repository.MovieRepository;
import com.spring.springboot2.repository.SpringUserRepository;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.util.MovieCreator;
import com.spring.springboot2.util.MoviePostRequestBodyCreator;
import com.spring.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MovieControllerIT {
    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private SpringUserRepository springUserRepository;
    private static final SpringUser USER = SpringUser.builder()
            .name("Dev")
            .password("{bcrypt}$2a$10$6JDU8S5ehQXAPDjHfDDgCO.bunsGz1TZkd.WgSzcFB0BlZqtR6BWe")
            .username("dev2")
            .authorities("ROLE_USER")
            .build();

    private static final SpringUser ADMIN = SpringUser.builder()
            .name("Julius")
            .password("{bcrypt}$2a$10$6JDU8S5ehQXAPDjHfDDgCO.bunsGz1TZkd.WgSzcFB0BlZqtR6BWe")
            .username("julius2")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("dev2", "123");
            return new TestRestTemplate(restTemplateBuilder);
        }
        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("julius2", "123");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("list returns list of movie inside page object when successful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(USER);

        String expectedName = savedMovie.getName();

        PageableResponse<Movie> moviePage = testRestTemplateRoleUser.exchange("/movies", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Movie>>() {
                }).getBody();

        Assertions.assertThat(moviePage).isNotNull();

        Assertions.assertThat(moviePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of movie when successful")
    void listAll_ReturnsListOfMovies_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(USER);

        String expectedName = savedMovie.getName();

        List<Movie> movies = testRestTemplateRoleUser.exchange("/movies/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertThat(movies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns movie when successful")
    void findById_ReturnsMovies_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(USER);

        Long expectedId = savedMovie.getId();

        Movie movie = testRestTemplateRoleUser.getForObject("/movies/{id}", Movie.class, expectedId);

        Assertions.assertThat(movie).isNotNull();

        Assertions.assertThat(movie.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of movie when successful")
    void findByName_ReturnsListOfMovie_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(USER);

        String expectedName = savedMovie.getName();

        String url = String.format("/movies/find?name=%s", expectedName);

        List<Movie> movies = testRestTemplateRoleUser.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertThat(movies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of movie when movie is not found")
    void findByName_ReturnsEmptyListOfMovie_WhenMovieIsNotFound() {
        springUserRepository.save(USER);

        List<Movie> movies = testRestTemplateRoleUser.exchange("/movies/find?name=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertThat(movies)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns movie when successful")
    void save_ReturnsMovie_WhenSuccessful() {
        springUserRepository.save(USER);

        MoviePostRequestBody moviePostRequestBody = MoviePostRequestBodyCreator.createMoviePostRequestBody();

        ResponseEntity<Movie> movieResponseEntity = testRestTemplateRoleUser.postForEntity("/movies", moviePostRequestBody, Movie.class);

        Assertions.assertThat(movieResponseEntity).isNotNull();
        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(movieResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(movieResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("replace updates movie when successful")
    void replace_UpdatesMovie_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(USER);

        savedMovie.setName("new name");

        ResponseEntity<Void> movieResponseEntity = testRestTemplateRoleUser.exchange("/movies",
                HttpMethod.PUT, new HttpEntity<>(savedMovie), Void.class);

        Assertions.assertThat(movieResponseEntity).isNotNull();

        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(ADMIN);

        ResponseEntity<Void> movieResponseEntity = testRestTemplateRoleAdmin.exchange("/movies/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedMovie.getId());

        Assertions.assertThat(movieResponseEntity).isNotNull();

        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("delete returns 403 when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());
        springUserRepository.save(USER);

        ResponseEntity<Void> movieResponseEntity = testRestTemplateRoleUser.exchange("/movies/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedMovie.getId());

        Assertions.assertThat(movieResponseEntity).isNotNull();

        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}