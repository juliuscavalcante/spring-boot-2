package com.spring.springboot2.repository;

import com.spring.springboot2.domain.Movie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for Movie Repository")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    @DisplayName("Save creates movie when successful")
    void save_PersistMovie_WhenSuccessful() {
        Movie movieToBeSaved = createMovie();
        Movie movieSaved = this.movieRepository.save(movieToBeSaved);
        Assertions.assertThat(movieSaved).isNotNull();
        Assertions.assertThat(movieSaved.getId()).isNotNull();
        Assertions.assertThat(movieSaved.getName()).isEqualTo(movieToBeSaved.getName());
    }








    private Movie createMovie() {
        return Movie.builder()
                .name("Dunkirk")
                .build();
    }
}