package com.spring.springboot2.controller;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.requests.MoviePutRequestBody;
import com.spring.springboot2.service.MovieService;
import com.spring.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("movies")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final DateUtil dateUtil;
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> findAll() {
        log.info(dateUtil.formatLocalDateTimeToDatebaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Movie> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Movie>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(movieService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Movie> save(@RequestBody @Valid MoviePostRequestBody movie) {
        return new ResponseEntity<>(movieService.save(movie), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody MoviePutRequestBody moviePutRequestBody) {
        movieService.replace(moviePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
