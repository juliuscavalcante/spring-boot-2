package com.spring.springboot2.service;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MovieService {

    private List<Movie> movies = List.of(new Movie(1L,"The Dark Knight"), new Movie(2L,"The Prestige"));

    //private final MovieRepository movieRepository;

    public List<Movie> findAll() {
        return movies;
    }

    public Movie findById(Long id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie id not found"));
    }
}
