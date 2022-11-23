package com.spring.springboot2.service;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.exception.BadRequestException;
import com.spring.springboot2.mapper.MovieMapper;
import com.spring.springboot2.repository.MovieRepository;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.requests.MoviePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public List<Movie> findByName(String name) {
        return movieRepository.findByName(name);
    }

    public Movie findByIdOrThrowBadRequestException(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Movie id not found"));
    }

    public Movie save(MoviePostRequestBody moviePostRequestBody) {
        return movieRepository.save(MovieMapper.INSTANCE.toMovie(moviePostRequestBody));
    }

    public void delete(Long id) {
        movieRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(MoviePutRequestBody moviePutRequestBody) {
        Movie savedMovie = findByIdOrThrowBadRequestException(moviePutRequestBody.getId());
        Movie movie = MovieMapper.INSTANCE.toMovie(moviePutRequestBody);
        movie.setId(savedMovie.getId());
        movieRepository.save(movie);
    }
}
