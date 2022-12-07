package com.spring.springboot2.service;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.exception.BadRequestException;
import com.spring.springboot2.repository.MovieRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Movie> moviePage = new PageImpl<>(List.of(MovieCreator.createValidMovie()));
        BDDMockito.when(movieRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(moviePage);

        BDDMockito.when(movieRepositoryMock.findAll())
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(MovieCreator.createValidMovie()));

        BDDMockito.when(movieRepositoryMock.save(ArgumentMatchers.any(Movie.class)))
                .thenReturn(MovieCreator.createValidMovie());

        BDDMockito.doNothing().when(movieRepositoryMock).delete(ArgumentMatchers.any(Movie.class));
    }

    @Test
    @DisplayName("listAll returns list of movie inside page object when sucessful")
    void listAll_ReturnsListOfMoviesInsidePageObject_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        Page<Movie> moviePage = movieService.listAll(PageRequest.of(1,1));

        Assertions.assertThat(moviePage).isNotNull();
        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(moviePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of movie when sucessful")
    void listAllNonPageable_ReturnsListOfMovies_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        List<Movie> movies = movieService.listAllNonPageable();

        Assertions.assertThat(movies).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when movie is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenMovieIsNotFound() {

        BDDMockito.when(movieRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> this.movieService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("findByName returns a list of movie when sucessful")
    void findByName_ReturnsAListOfMovie_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        List<Movie> movies = movieService.findByName("movie");

        Assertions.assertThat(movies).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(movies.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list when movie is not found")
    void findByName_ReturnsAnEmptyList_WhenMovieIsNotFound() {

        BDDMockito.when(movieRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Movie> movies = movieService.findByName("movie");
        Assertions.assertThat(movies).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns a movie when sucessful")
    void save_ReturnsAMovie_WhenSuccessful() {

        Movie movie = movieService.save(MoviePostRequestBodyCreator.createMoviePostRequestBody());

        Assertions.assertThat(movie).isNotNull().isEqualTo(MovieCreator.createValidMovie());
    }

    @Test
    @DisplayName("replace updates a movie when sucessful")
    void replace_UpdatesAMovie_WhenSuccessful() {

        Assertions.assertThatCode(() -> movieService.replace(
                MoviePutRequestBodyCreator.createMoviePutRequestBody())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes a movie when sucessful")
    void delete_RemovesAMovie_WhenSuccessful() {

        Assertions.assertThatCode(() -> movieService.delete(1L)).doesNotThrowAnyException();
    }
}

