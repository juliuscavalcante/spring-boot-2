package com.spring.springboot2.mapper;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.requests.MoviePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class MovieMapper {

    public static final MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    public abstract Movie toMovie(MoviePostRequestBody moviePostRequestBody);

    public abstract Movie toMovie(MoviePutRequestBody moviePutRequestBody);
}
