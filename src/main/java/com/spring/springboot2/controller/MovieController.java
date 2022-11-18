package com.spring.springboot2.controller;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.service.MovieService;
import com.spring.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(movieService.findById(id));
    }
}
