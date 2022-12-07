package com.spring.springboot2.requests;


import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class MoviePostRequestBody {

    @NotBlank(message = "Movie name can't be empty or null")
    private String name;

}
