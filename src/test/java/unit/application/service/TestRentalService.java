package unit.application.service;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.application.services.MovieService;
import application.application.services.RentalService;
import application.domain.entities.movie.Movie;
import application.domain.entities.rental.Rental;
import application.repositories.inmemory.MovieRepository;
import application.repositories.inmemory.RentalRepository;

import java.time.LocalDate;

// Only one spy here – this one is unique in the package
class RentalRepositorySpy extends RentalRepository {}

public class TestRentalService {

    private RentalRepository rentalRepo;
    private MovieRepository movieRepo;
    private RentalService service;
    private MovieService movieService;
    private Rental rental;
    private Movie movie;

    @BeforeEach
    public void setUpRepo() {
        rentalRepo = new RentalRepositorySpy();
        // use the real in-memory repository; no spy needed here
        movieRepo = new MovieRepository();

        movieService = new MovieService(movieRepo);
        service = new RentalService(rentalRepo, movieService);

        movie = new Movie("Barbie Movie");
        rental = new Rental(movie);
    }

    @Test
    public void mustSaveRental() {
        service.save(rental);

        Optional<Rental> optionalSavedRental = rentalRepo.findById(rental.getId());

        Assertions.assertTrue(optionalSavedRental.isPresent());

        Rental savedRental = optionalSavedRental.get();

        Assertions.assertNotNull(savedRental);
        Assertions.assertEquals(rental.getId(), savedRental.getId());
    }

    @Test
    public void mustFindRentalById() {
        rentalRepo.save(rental);

        Rental savedRental = Assertions.assertDoesNotThrow(
                () -> service.findById(rental.getId())
        );

        Assertions.assertNotNull(savedRental);
    }

    @Test
    public void mustFindRentalsByMovieName() {
        service.save(rental);
        Movie rentedMovie = rental.getRentedMovie();
        ArrayList<Rental> foundRentals = service.findByMovieName(rentedMovie.getName());

        Assertions.assertTrue(foundRentals.contains(rental));
    }

    @Test
    public void mustHaveFeeToBePaid() {
        // movie is rented for a week automatically, 10 days after rental should be late
        service.save(rental);
        service.returnMovie(rental, LocalDate.now().plusDays(10));

        Assertions.assertEquals(20.00, rental.getLateFee(), 0.0001);
    }

    @Test
    public void mustPayFee() {
        service.save(rental);
        service.returnMovie(rental, LocalDate.now().plusDays(10));

        service.payLateFee(rental);

        Assertions.assertTrue(service.isLateFeePaid(rental));
    }
}
