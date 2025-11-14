package application.presentation.controllers;

import application.application.services.MovieService;
import application.application.services.RentalService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import application.domain.entities.movie.Movie;
import application.domain.entities.rental.Rental;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final MovieService movieService;

    public RentalController(RentalService rentalService, MovieService movieService) {
        this.rentalService = rentalService;
        this.movieService = movieService;
    }

    public static class CreateRentalRequest {
        private int movieId;
        public int getMovieId() { return movieId; }
        public void setMovieId(int movieId) { this.movieId = movieId; }
    }

    @PostMapping
    public Rental createRental(@RequestBody CreateRentalRequest request) {
        return rentalService.createRentalForMovie(request.getMovieId());
    }

    @GetMapping
    public ArrayList<Rental> listOrSearch(
            @RequestParam(value = "movieName", required = false) String movieName) {
        if (movieName == null || movieName.isBlank()) {
            return rentalService.findAll();
        }
        return rentalService.findByMovieName(movieName);
    }

    @GetMapping("/{id}")
    public Rental getById(@PathVariable("id") int id) {
        return rentalService.findById(id);
    }

    @PostMapping("/{id}/return")
    public Rental returnRental(
            @PathVariable("id") int id,
            @RequestParam("returnDate") String returnDate) {

        Rental rental = rentalService.findById(id);
        LocalDate parsedDate = LocalDate.parse(returnDate);
        rentalService.returnMovie(rental, parsedDate);
        return rental;
    }

    @PostMapping("/{id}/pay-late-fee")
    public Rental payLateFee(@PathVariable("id") int id) {
        Rental rental = rentalService.findById(id);
        rentalService.payLateFee(rental);
        return rental;
    }
}
