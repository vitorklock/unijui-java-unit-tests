package unit.application.service;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import domain.entities.movie.Rental;
import application.services.RentalService;
import domain.entities.movie.Movie;
import repositories.inmemory.RentalRepository;
import java.time.LocalDate;

class RentalRepositorySpy extends RentalRepository{}

public class TestRentalService {
    private RentalRepository repo;
    private RentalService service;
    private Rental rental;
    private Movie movie;

    @BeforeEach
    public void setUpRepo() {
        repo = new RentalRepositorySpy();
        service = new RentalService(repo);
        movie = new Movie("Barbie Movie");
        rental = new Rental(movie);
    }
    
    @Test
    public void mustSaveRental(){
        service.save(rental);

        Optional<Rental> optionalSavedRental = repo.findById(rental.getId());

        Assertions.assertTrue(optionalSavedRental.isPresent());

        Rental savedRental = optionalSavedRental.get();

        Assertions.assertNotNull(savedRental);
        Assertions.assertEquals(rental.getId(), savedRental.getId());
        
    }
    
    @Test
    public void mustFindRentalById(){
        repo.save(rental);

        Rental savedRental = Assertions.assertDoesNotThrow(() -> service.findById(rental.getId()));

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
        //movie is rented for a week automatically, 10 days after rental should be late
        rental.setReturnDate(LocalDate.now().plusDays(10));
        
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
