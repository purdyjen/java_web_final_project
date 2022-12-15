package com.example.java_web_final_project.repository;

import com.example.java_web_final_project.repository.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
