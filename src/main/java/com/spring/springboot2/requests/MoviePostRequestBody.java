package com.spring.springboot2.requests;


import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Data
public class MoviePostRequestBody {

    @NotBlank(message = "Movie name can't be empty or null")
    private String name;

}
