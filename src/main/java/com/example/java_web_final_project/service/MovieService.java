package com.example.java_web_final_project.service;

import com.example.java_web_final_project.repository.MovieRepository;
import com.example.java_web_final_project.repository.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie save(Movie movie) {return movieRepository.save(movie);}

    public void delete (int movieId) {movieRepository.deleteById(movieId);}

    public List<Movie> all() {return movieRepository.findAll();}

    public Movie findById(int movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        boolean isPresent = movie.isPresent();
        if (isPresent) {
            return movie.get();
        } else {
            return null;
        }
    }
}
