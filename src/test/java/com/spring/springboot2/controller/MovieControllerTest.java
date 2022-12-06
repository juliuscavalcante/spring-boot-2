package com.spring.springboot2.controller;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.service.MovieService;
import com.spring.springboot2.util.MovieCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    @DisplayName("List returns list of movie inside page object when sucessful")
    void list_ReturnsListOfMoviesInsidePageObject_WhenSuccessful() {

        String expectedName = MovieCreator.createValidMovie().getName();
        Page<Movie> moviePage = movieController.list(null).getBody();

        Assertions.assertThat(moviePage).isNotNull();
        Assertions.assertThat(moviePage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(moviePage.toList().get(0).getName()).isEqualTo(expectedName);
    }
}