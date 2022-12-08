package com.spring.springboot2.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoviePostRequestBody {

    @NotBlank(message = "Movie name can't be empty or null")
    private String name;

}
