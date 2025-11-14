package integration;

import application.application.services.MovieService;
import application.application.services.RentalService;
import application.domain.entities.movie.Movie;
import application.domain.entities.rental.Rental;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import application.repositories.inmemory.MovieRepository;
import application.repositories.inmemory.RentalRepository;

// Simple “IT” repositories to keep symmetry with the unit tests
class MovieRepositoryIT extends MovieRepository {}
class RentalRepositoryIT extends RentalRepository {}

public class TestMovieRentalIntegration {

    private MovieRepository movieRepo;
    private RentalRepository rentalRepo;
    private MovieService movieService;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        movieRepo = new MovieRepositoryIT();
        rentalRepo = new RentalRepositoryIT();
        movieService = new MovieService(movieRepo);
        rentalService = new RentalService(rentalRepo);
    }

    // -------------------------------------------------------------------------
    // SCENARIO 1 – Customer rents an available movie and uses search features
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Scenario 1.1 - Customer rents an available movie: rental is created and movie becomes unavailable")
    void scenario1_customerRentsAvailableMovie() {
        // GIVEN an available movie saved in the system
        Movie movie = new Movie("Barbie Movie");
        movieService.save(movie);

        // WHEN the customer rents the movie (create rental) and the movie is marked as rented
        Rental rental = new Rental(movie);
        rentalService.save(rental);       // sets startDate and endDate
        movieService.rentMovie(movie);    // marks movie as unavailable

        // THEN the movie is unavailable and the rental is persisted with rental dates
        Optional<Movie> savedMovieOpt = movieRepo.findById(movie.getId());
        Optional<Rental> savedRentalOpt = rentalRepo.findById(rental.getId());

        Assertions.assertTrue(savedMovieOpt.isPresent());
        Assertions.assertTrue(savedRentalOpt.isPresent());

        Movie savedMovie = savedMovieOpt.get();
        Rental savedRental = savedRentalOpt.get();

        Assertions.assertFalse(savedMovie.isAvailable(), "Movie must be unavailable after being rented");
        Assertions.assertNotNull(savedRental.getStartDate(), "Rental must have a start date");
        Assertions.assertNotNull(savedRental.getEndDate(), "Rental must have an end date");
    }

    @Test
    @DisplayName("Scenario 1.2 - Must not allow renting the same movie twice while it is already rented")
    void scenario1_mustNotRentAlreadyRentedMovie() {
        // GIVEN a movie saved and already rented
        Movie movie = new Movie("Barbie Movie");
        movieService.save(movie);

        Rental rental = new Rental(movie);
        rentalService.save(rental);
        movieService.rentMovie(movie);  // first rental

        // WHEN trying to rent the same movie again
        // THEN an IllegalStateException must be thrown
        IllegalStateException ex = Assertions.assertThrows(
                IllegalStateException.class,
                () -> movieService.rentMovie(movie)
        );

        Assertions.assertTrue(
                ex.getMessage().contains("already rented"),
                "Error message should indicate that the movie is already rented"
        );
    }

    @Test
    @DisplayName("Scenario 1.3 - Must be able to search movies and rentals by movie name")
    void scenario1_canSearchMovieAndRentalByName() {
        // GIVEN a movie and a rental saved
        Movie movie = new Movie("Barbie Movie");
        movieService.save(movie);

        Rental rental = new Rental(movie);
        rentalService.save(rental);
        movieService.rentMovie(movie);

        // WHEN searching by (partial) movie name
        ArrayList<Movie> moviesFound = movieService.findByName("barbie");
        ArrayList<Rental> rentalsFound = rentalService.findByMovieName("Barbie");

        // THEN both searches must contain the correct movie/rental
        Assertions.assertTrue(
                moviesFound.stream().anyMatch(m -> m.getId() == movie.getId()),
                "Movie search should contain the rented movie"
        );

        Assertions.assertTrue(
                rentalsFound.stream().anyMatch(r -> r.getId() == rental.getId()),
                "Rental search should contain the created rental"
        );
    }

    // -------------------------------------------------------------------------
    // SCENARIO 2 – Customer returns a movie late, gets a fee, and pays it
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Scenario 2.1 - Late return must generate a fixed late fee of 20")
    void scenario2_lateReturnGeneratesFee() {
        // GIVEN a movie rented for 7 days (rule in Rental.updateRentalDates via RentalService.save)
        Movie movie = new Movie("Matrix");
        movieService.save(movie);

        Rental rental = new Rental(movie);
        rentalService.save(rental);      // startDate = today, endDate = today + 7
        movieService.rentMovie(movie);

        // WHEN the customer returns the movie 3 days after the end date
        LocalDate lateReturnDate = rental.getEndDate().plusDays(3);
        rentalService.returnMovie(rental, lateReturnDate);

        // THEN the late fee must be 20 and not yet paid
        Assertions.assertEquals(20.0, rental.getLateFee(), 0.0001, "Late fee should be 20");
        Assertions.assertFalse(rental.isPaidFee(), "Late fee must not be marked as paid initially");
    }

    @Test
    @DisplayName("Scenario 2.2 - After paying the late fee, rental must indicate that the fee is paid")
    void scenario2_payingLateFeeMarksAsPaid() {
        // GIVEN a rental with a late return and a generated fee
        Movie movie = new Movie("Lord of the Rings");
        movieService.save(movie);

        Rental rental = new Rental(movie);
        rentalService.save(rental);
        movieService.rentMovie(movie);

        LocalDate lateReturnDate = rental.getEndDate().plusDays(5);
        rentalService.returnMovie(rental, lateReturnDate); // generates late fee = 20

        Assertions.assertEquals(20.0, rental.getLateFee(), 0.0001);

        // WHEN the customer pays the late fee
        rentalService.payLateFee(rental);

        // THEN the system must mark the late fee as paid
        Assertions.assertTrue(
                rentalService.isLateFeePaid(rental),
                "Late fee should be marked as paid"
        );
    }
}
