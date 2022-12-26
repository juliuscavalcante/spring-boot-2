package com.spring.springboot2.controller;

import com.spring.springboot2.domain.Movie;
import com.spring.springboot2.requests.MoviePostRequestBody;
import com.spring.springboot2.requests.MoviePutRequestBody;
import com.spring.springboot2.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("movies")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    @Operation(summary = "List all movies paginated",
            description = "The default size is 20, use the parameter size to change the default value")
    public ResponseEntity<Page<Movie>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(movieService.listAll(pageable));
    }

    @GetMapping(path = "/all")
    @Operation(summary = "List all movies")
    public ResponseEntity<List<Movie>> listAll() {
        return ResponseEntity.ok(movieService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Find a movie by ID")
    public ResponseEntity<Movie> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/by-id/{id}")
    public ResponseEntity<Movie> findByIdAuthenticationPrincipal(@PathVariable Long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails);
        return ResponseEntity.ok(movieService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/find")
    @Operation(summary = "Find a movie by name")
    public ResponseEntity<List<Movie>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(movieService.findByName(name));
    }

    @PostMapping
    @Operation(summary = "Save a movie")
    public ResponseEntity<Movie> save(@RequestBody @Valid MoviePostRequestBody movie) {
        return new ResponseEntity<>(movieService.save(movie), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/{id}")
    @Operation(summary = "Delete a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "When Movie does not exist in the Database")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @Operation(summary = "Updates a movie")
    public ResponseEntity<Void> replace(@RequestBody MoviePutRequestBody moviePutRequestBody) {
        movieService.replace(moviePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
