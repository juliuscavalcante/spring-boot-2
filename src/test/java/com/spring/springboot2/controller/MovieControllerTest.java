package com.spring.springboot2.controller;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.requests.MoviePutRequestBody;
import com.spring.springboot2.service.MovieService;
import com.spring.springboot2.util.MovieCreator;
import com.spring.springboot2.util.MoviePostRequestBodyCreator;
import com.spring.springboot2.util.MoviePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class MovieControllerTest {

    @InjectMocks
    private MovieController movieController;

    @Mock
    private MovieService movieServiceMock;
    
    @BeforeEach
    void setUp() {
        PageImpl<Movie> moviePage = new PageImpl<>(List.of(MovieCreator.createValidMovie()));
        BDDMockito.when(movieServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(moviePage);

        BDDMockito.when(movieServiceMock.listAllNonPageable())
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn((MovieCreator.createValidMovie()));

        BDDMockito.when(movieServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieServiceMock.save(ArgumentMatchers.any(MoviePostRequestBody.class)))
                .thenReturn(MovieCreator.createValidMovie());

        BDDMockito.doNothing().when(movieServiceMock).replace(ArgumentMatchers.any(MoviePutRequestBody.class));

        BDDMockito.doNothing().when(movieServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of movie inside page object when sucessful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        Page<Movie> moviePage = movieController.list(null).getBody();

        Assertions.assertThat(moviePage).isNotNull();
        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(moviePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of movie when sucessful")
    void listAll_ReturnsListOfMovies_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        List<Movie> movies = movieController.listAll().getBody();

        Assertions.assertThat(movies).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns a movie when sucessful")
    void findById_ReturnsAMovie_WhenSuccessful() {

        Long expectedId = MovieCreator.createValidMovie().getId();
        Movie movie = movieController.findById(1L).getBody();

        Assertions.assertThat(movie).isNotNull();
        Assertions.assertThat(movie.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of movie when sucessful")
    void findByName_ReturnsAListOfMovie_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        List<Movie> movies = movieController.findByName("movie").getBody();

        Assertions.assertThat(movies).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list when movie is not found")
    void findByName_ReturnsAnEmptyList_WhenMovieIsNotFound() {

        BDDMockito.when(movieServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Movie> movies = movieController.findByName("movie").getBody();
        Assertions.assertThat(movies).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns a movie when sucessful")
    void save_ReturnsAMovie_WhenSuccessful() {

        Movie movie = movieController.save(MoviePostRequestBodyCreator.createMoviePostRequestBody()).getBody();

        Assertions.assertThat(movie).isNotNull().isEqualTo(MovieCreator.createValidMovie());
    }

    @Test
    @DisplayName("replace updates a movie when sucessful")
    void replace_UpdatesAMovie_WhenSuccessful() {

        Assertions.assertThatCode(() -> movieController.replace(
                MoviePutRequestBodyCreator.createMoviePutRequestBody())).doesNotThrowAnyException();

        ResponseEntity<Void> entity = movieController.replace(MoviePutRequestBodyCreator.createMoviePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes a movie when sucessful")
    void delete_RemovesAMovie_WhenSuccessful() {

        Assertions.assertThatCode(() -> movieController.delete(1L)).doesNotThrowAnyException();

        ResponseEntity<Void> entity = movieController.delete(1L);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}