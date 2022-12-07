package com.spring.springboot2.util;

import com.spring.springboot2.requests.MoviePutRequestBody;

public class MoviePutRequestBodyCreator {

    public static MoviePutRequestBody createMoviePutRequestBody() {
        return MoviePutRequestBody.builder()
                .id(MovieCreator.createValidUpdatedMovie().getId())
                .name(MovieCreator.createValidUpdatedMovie().getName())
                .build();
    }
}
