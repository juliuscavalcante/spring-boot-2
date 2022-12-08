package com.spring.springboot2.integration;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.repository.MovieRepository;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.util.MovieCreator;
import com.spring.springboot2.util.MoviePostRequestBodyCreator;
import com.spring.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
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
public class MovieControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("list returns list of movie inside page object when sucessful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful() {

        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        String expectedName = savedMovie.getName();
        PageableResponse<Movie> moviePage = testRestTemplate.exchange("/movies", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Movie>>()  {
        }).getBody();

        Assertions.assertThat(moviePage).isNotNull();
        Assertions.assertThat(moviePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of movie when sucessful")
    void listAll_ReturnsListOfMovies_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        String expectedName = savedMovie.getName();
        List<Movie> movies = testRestTemplate.exchange("/movies/all", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Movie>>()  {
                }).getBody();

        Assertions.assertThat(movies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns a movie when sucessful")
    void findById_ReturnsAMovie_WhenSuccessful() {
        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        Long expectedId = savedMovie.getId();
        Movie movie = testRestTemplate.getForObject("/movies/{id}", Movie.class, expectedId);

        Assertions.assertThat(movie).isNotNull();
        Assertions.assertThat(movie.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of movie when sucessful")
    void findByName_ReturnsAListOfMovie_WhenSuccessful() {

        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        String expectedName = savedMovie.getName();

        String url = String.format("/movies/find?name=%s", expectedName);

        List<Movie> movies = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertThat(movies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list when movie is not found")
    void findByName_ReturnsAnEmptyList_WhenMovieIsNotFound() {

        List<Movie> movies = testRestTemplate.exchange("/movies/find?name=inception", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Movie>>() {
                }).getBody();

        Assertions.assertThat(movies)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns a movie when sucessful")
    void save_ReturnsAMovie_WhenSuccessful() {

        MoviePostRequestBody moviePostRequestBody = MoviePostRequestBodyCreator.createMoviePostRequestBody();

        ResponseEntity<Movie> movieResponseEntity = testRestTemplate.postForEntity("/movies",
                moviePostRequestBody, Movie.class);

        Assertions.assertThat(movieResponseEntity).isNotNull();
        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(movieResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(movieResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates a movie when sucessful")
    void replace_UpdatesAMovie_WhenSuccessful() {

        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        savedMovie.setName("new name");

        ResponseEntity<Void> movieResponseEntity = testRestTemplate.exchange("/movies",
                HttpMethod.PUT,new HttpEntity<>(savedMovie), Void.class);

        Assertions.assertThat(movieResponseEntity).isNotNull();

        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes a movie when sucessful")
    void delete_RemovesAMovie_WhenSuccessful() {

        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        ResponseEntity<Void> movieResponseEntity = testRestTemplate.exchange("/movies/{id}",
                HttpMethod.DELETE,null, Void.class, savedMovie.getId());

        Assertions.assertThat(movieResponseEntity).isNotNull();

        Assertions.assertThat(movieResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
