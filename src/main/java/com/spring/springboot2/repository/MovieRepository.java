package com.spring.springboot2.repository;

import com.spring.springboot2.domain.Movie;

import java.util.List;

public interface MovieRepository {

    List<Movie> listAll();
}
