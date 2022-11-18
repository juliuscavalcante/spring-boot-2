package com.spring.springboot2.service;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    //private final MovieRepository movieRepository;

    public List<Movie> listAll() {
        return List.of(new Movie(1L,"The Dark Knight"), new Movie(2L,"The Prestige"));
    }
}
