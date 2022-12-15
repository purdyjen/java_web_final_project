package com.example.java_web_final_project.controller;

import com.example.java_web_final_project.controller.dto.MovieDto;
import com.example.java_web_final_project.repository.MovieRepository;
import com.example.java_web_final_project.repository.entity.Movie;
import com.example.java_web_final_project.service.MovieService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class MovieController {
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final MovieModelAssembler assembler;

    public MovieController(MovieRepository movieRepository, MovieService movieService, MovieModelAssembler assembler) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.assembler = assembler;
    }

//    @GetMapping()
//    public String sayHello() {return "Test";}
    @GetMapping(path="/movies")
    CollectionModel<EntityModel<Movie>> all() {
        List<EntityModel<Movie>> movies = movieRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(movies, linkTo(methodOn(MovieController.class).all()).withSelfRel());
    }

    @PostMapping("/movies")
    ResponseEntity<?> newMovie(@RequestBody MovieDto movieDto) {

        EntityModel<Movie> entityModel = assembler.toModel(movieRepository.save(new Movie(movieDto)));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @GetMapping("/movies/{id}")
    EntityModel<Movie> one(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        return assembler.toModel(movie);
    }

    @PutMapping("/movies/{id}")
    ResponseEntity<?> replaceProduct(@RequestBody MovieDto movieDto, @PathVariable Integer id) {

        Movie updatedMovie = movieRepository.findById(id) //
                .map(movie -> {
                    movie.setName(movieDto.getName());
                    movie.setYear( movieDto.getYear() );
                    return movieRepository.save(movie);
                }) //
                .orElseThrow(() -> new MovieNotFoundException(id));

        EntityModel<Movie> entityModel = assembler.toModel(updatedMovie);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/movies/{id}")
    ResponseEntity<?> deleteMovie(@PathVariable Integer id) {

        movieRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
