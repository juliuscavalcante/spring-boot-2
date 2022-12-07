package com.spring.springboot2.integration;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.repository.MovieRepository;
import com.spring.springboot2.util.MovieCreator;
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
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class MovieControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplatet;

    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("list returns list of movie inside page object when sucessful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful() {

        Movie savedMovie = movieRepository.save(MovieCreator.createMovieToBeSaved());

        String expectedName = savedMovie.getName();
        PageableResponse<Movie> moviePage = testRestTemplatet.exchange("/movies", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Movie>>()  {
        }).getBody();

        Assertions.assertThat(moviePage).isNotNull();
        Assertions.assertThat(moviePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(moviePage.toList().get(0).getName()).isEqualTo(expectedName);
    }
}
