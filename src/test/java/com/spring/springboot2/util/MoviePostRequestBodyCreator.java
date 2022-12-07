package com.spring.springboot2.util;

import com.spring.springboot2.requests.MoviePostRequestBody;

public class MoviePostRequestBodyCreator {

    public static MoviePostRequestBody createMoviePostRequestBody() {
        return MoviePostRequestBody.builder()
                .name(MovieCreator.createMovieToBeSaved().getName())
                .build();
    }
}
