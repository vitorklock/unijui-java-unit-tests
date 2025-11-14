package application.presentation.controllers;

import application.application.services.MovieService;
import application.application.services.RentalService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import application.domain.entities.movie.Movie;
import application.domain.entities.rental.Rental;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final RentalService rentalService;

    public MovieController(MovieService movieService, RentalService rentalService) {
        this.movieService = movieService;
        this.rentalService = rentalService;
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
    public Movie getById(@PathVariable("id") int id) {
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
    public Rental rentMovie(@PathVariable("id") int id) {
        return rentalService.createRentalForMovie(id);
    }
}
