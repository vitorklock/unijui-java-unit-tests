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
import static org.mockito.Mockito.*;


public class TestRentalService {

    private RentalRepository rentalRepo;
    private MovieRepository movieRepo;
    private RentalService service;
    private MovieService movieService;
    private Rental rental;
    private Movie movie;

    @BeforeEach
    public void setUpRepo() {
        rentalRepo = mock(RentalRepository.class);
        movieRepo = mock(MovieRepository.class);
        movieService = mock(MovieService.class);

        service = new RentalService(rentalRepo, movieService);

        movie = mock(Movie.class);
        when(movie.getName()).thenReturn("Barbie Movie");
        
        rental = mock(Rental.class);
        when(rental.getId()).thenReturn(1);
        when(rental.getRentedMovie()).thenReturn(movie);
    }

    @Test
    public void mustSaveRental() {
        when(rentalRepo.save(rental)).thenAnswer(invocation -> {
            Rental r = invocation.getArgument(0);
            return r;
        });
        when(rentalRepo.findById(rental.getId())).thenAnswer(invocation -> {
            int id = invocation.getArgument(0);
            return id == rental.getId() ? Optional.of(rental) : Optional.empty();
        });

        Rental savedResult = service.save(rental);

        Assertions.assertNotNull(savedResult);
        Assertions.assertEquals(rental.getId(), savedResult.getId());
        
        verify(rentalRepo).save(rental);
        
        Optional<Rental> retrieved = rentalRepo.findById(rental.getId());
        Assertions.assertTrue(retrieved.isPresent());
        Assertions.assertEquals(rental.getId(), retrieved.get().getId());
    }

    @Test
    public void mustFindRentalById() {
        when(rentalRepo.findById(1)).thenReturn(Optional.of(rental));

        Rental savedRental = Assertions.assertDoesNotThrow(
                () -> service.findById(1)
        );

        Assertions.assertNotNull(savedRental);
        Assertions.assertEquals(1, savedRental.getId());
        
        verify(rentalRepo).findById(1);
    }

    @Test
    public void mustFindRentalsByMovieName() {
        ArrayList<Rental> mockList = new ArrayList<>();
        mockList.add(rental);
        when(rentalRepo.findAll()).thenReturn(mockList);

        ArrayList<Rental> foundRentals = service.findByMovieName("Barbie Movie");

        Assertions.assertTrue(foundRentals.contains(rental));
        
        verify(rentalRepo).findAll();
    }

    @Test
    public void mustHaveFeeToBePaid() {
        when(rentalRepo.save(rental)).thenReturn(rental);
        when(rental.getLateFee()).thenReturn(20.00);

        service.save(rental);
        service.returnMovie(rental, LocalDate.now().plusDays(10));

        Assertions.assertEquals(20.00, rental.getLateFee(), 0.0001);
        
    }

    @Test
    public void mustPayFee() {
        when(rentalRepo.save(rental)).thenReturn(rental);
        when(rental.isPaidFee()).thenReturn(true);

        service.save(rental);
        service.returnMovie(rental, LocalDate.now().plusDays(10));

        service.payLateFee(rental);

        Assertions.assertTrue(service.isLateFeePaid(rental));
        
        verify(rental).setPaidFee(true);
    }
}
