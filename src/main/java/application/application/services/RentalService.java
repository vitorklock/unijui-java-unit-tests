package application.application.services;

import application.domain.entities.movie.Movie;
import application.domain.entities.rental.Rental;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import application.repositories.inmemory.RentalRepository;

@Service
public class RentalService {

    private final RentalRepository rentals;
    private final MovieService movieService;

    public RentalService(RentalRepository rentals, MovieService movieService) {
        this.rentals = rentals;
        this.movieService = movieService;
    }

    public Rental save(Rental rental) {
        Objects.requireNonNull(rental, "Rental cannot be null");
        rental.updateRentalDates();
        return rentals.save(rental);
    }

    public Rental findById(int id) {
        Objects.requireNonNull(id, "Must inform an Id");
        return rentals.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rental not found with id: " + id));
    }

    public ArrayList<Rental> findByMovieName(String movieName) {
        if (isNullOrBlank(movieName)) {
            return new ArrayList<>();
        }
        String searchName = movieName.toLowerCase().trim();

        return rentals.findAll().stream()
                .filter(rental -> rental.getRentedMovie().getName().toLowerCase().contains(searchName))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Rental createRentalForMovie(int movieId) {
        // find and mark movie as rented (unavailable)
        Movie movie = movieService.findById(movieId);
        movieService.rentMovie(movie); // uses existing logic & checks

        // create and persist rental
        Rental rental = new Rental(movie);
        rental.updateRentalDates();
        return rentals.save(rental);
    }

    public void returnMovie(Rental rental, LocalDate returnDate) {
        Objects.requireNonNull(rental, "Rental cannot be null");
        Objects.requireNonNull(returnDate, "Return date cannot be null");

        rental.updateReturn(returnDate);

        Movie movie = rental.getRentedMovie();
        if (movie != null) {
            movie.setAvailable(true);
            movieService.save(movie);
        }
    }

    public void payLateFee(Rental rental) {
        Objects.requireNonNull(rental, "Rental cannot be null");
        rental.setPaidFee(true);
    }

    public boolean isLateFeePaid(Rental rental) {
        Objects.requireNonNull(rental, "Rental cannot be null");
        return rental.isPaidFee();
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public ArrayList<Rental> findAll() {
        return new ArrayList<>(rentals.findAll());
    }
}
