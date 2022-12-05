package com.spring.springboot2.repository;

import com.spring.springboot2.domain.Movie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("Tests for Movie Repository")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("Save persists movie when successful")
    void save_PersistMovie_WhenSuccessful() {

        Movie movieToBeSaved = createMovie();
        Movie movieSaved = this.movieRepository.save(movieToBeSaved);
        Assertions.assertThat(movieSaved).isNotNull();
        Assertions.assertThat(movieSaved.getId()).isNotNull();
        Assertions.assertThat(movieSaved.getName()).isEqualTo(movieToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates movie when successful")
    void save_UpdatesMovie_WhenSuccessful() {

        Movie movieToBeSaved = createMovie();
        Movie movieSaved = this.movieRepository.save(movieToBeSaved);
        movieSaved.setName("Interstellar");
        Movie movieUpdated = this.movieRepository.save(movieSaved);
        Assertions.assertThat(movieUpdated).isNotNull();
        Assertions.assertThat(movieUpdated.getId()).isNotNull();
        Assertions.assertThat(movieUpdated.getName()).isEqualTo(movieSaved.getName());
    }

    @Test
    @DisplayName("Delete removes movie when successful")
    void delete_RemovesMovie_WhenSuccessful() {

        Movie movieToBeSaved = createMovie();
        Movie movieSaved = this.movieRepository.save(movieToBeSaved);
        this.movieRepository.delete(movieSaved);
        Optional<Movie> movieOptional = this.movieRepository.findById(movieSaved.getId());
        Assertions.assertThat(movieOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find by name returns list of movie when successful")
    void findByName_ReturnsListOfMovie_WhenSuccessful() {

        Movie movieToBeSaved = createMovie();
        Movie movieSaved = this.movieRepository.save(movieToBeSaved);
        String name = movieSaved.getName();
        List<Movie> movies = this.movieRepository.findByName(name);
        Assertions.assertThat(movies).isNotEmpty().contains(movieSaved);
    }

    @Test
    @DisplayName("Find by name returns an empty list when movie is not found")
    void findByName_ReturnsAnEmptyList_WhenMovieIsNotFound() {

        List<Movie> movies = this.movieRepository.findByName("Memento");
        Assertions.assertThat(movies).isEmpty();
    }

    @Test
    @DisplayName("Save throws ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {

        Movie movie = new Movie();
        Assertions.assertThatThrownBy(() -> this.movieRepository.save(movie))
                .isInstanceOf(ConstraintViolationException.class);
    }

    private Movie createMovie() {
        return Movie.builder()
                .name("Dunkirk")
                .build();
    }
}