package com.spring.springboot2.requests;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoviePutRequestBody {

    private Long id;

    private String name;

}
