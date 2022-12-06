package com.spring.springboot2.util;

import com.spring.springboot2.domain.Movie;

public class MovieCreator {

    public static Movie createMovieToBeSaved() {
        return Movie.builder()
                .name("Dunkirk")
                .build();
    }

    public static Movie createValidMovie() {
        return Movie.builder()
                .name("Dunkirk")
                .id(1L)
                .build();
    }

    public static Movie createValidUpdatedMovie() {
        return Movie.builder()
                .name("The Dark Knight")
                .id(1L)
                .build();
    }
}
