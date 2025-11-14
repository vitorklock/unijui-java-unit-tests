package application.presentation.controllers;

import application.application.services.MovieService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import application.domain.entities.movie.Movie;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public static class CreateMovieRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @PostMapping
    public Movie createMovie(@RequestBody CreateMovieRequest request) {
        Movie movie = new Movie(request.getName());
        return movieService.save(movie);
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable("id") int id) {   // 👈 explicit name
        return movieService.findById(id);
    }

    @GetMapping
    public ArrayList<Movie> listOrSearch(
            @RequestParam(value = "name", required = false) String name) {
        if (name == null || name.isBlank()) {
            return movieService.findAll();
        }
        return movieService.findByName(name);
    }

    @PostMapping("/{id}/rent")
    public Movie rentMovie(@PathVariable("id") int id) {  // 👈 explicit name
        Movie movie = movieService.findById(id);
        movieService.rentMovie(movie);
        return movie;
    }
}
