package com.example.java_web_final_project.controller;

public class MovieNotFoundException extends RuntimeException {
    MovieNotFoundException(Integer id) { super("Could not find movie " + id); }
}
