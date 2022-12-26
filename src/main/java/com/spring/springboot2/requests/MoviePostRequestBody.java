package com.spring.springboot2.requests;


import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "This is the Movie's name", example = "The Prestige")
    private String name;

}
