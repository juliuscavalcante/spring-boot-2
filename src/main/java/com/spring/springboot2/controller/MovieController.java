package com.spring.springboot2.controller;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private DateUtil dateUtil;

    @GetMapping(path = "list")
    public List<Movie> list() {
        return List.of(new Movie("Batman"), new Movie("The Judge"));
    }
}
