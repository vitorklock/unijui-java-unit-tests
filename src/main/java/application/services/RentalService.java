package application.services;

import domain.entities.movie.Movie;
import domain.entities.movie.Rental;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import repositories.inmemory.RentalRepository;


public class RentalService {
    private final RentalRepository rentals;
    
    public RentalService(RentalRepository rentals){
        this.rentals = rentals;
    }
    public Rental save(Rental rental) {
        rental.setStartDate(LocalDate.now());
        rental.setEndDate(LocalDate.now().plusDays(7));
        return rentals.save(rental);
    }
    
    public Rental findById(int id){
        return rentals.findById(id).orElseThrow(() -> new NoSuchElementException("Rental not found with id: " + id));
    }
    
    public ArrayList<Rental> findByMovieName(String movieName){
        String searchName = movieName.toLowerCase().trim();
        
        return rentals.findAll().stream()
                                        .filter(rental -> rental.getRentedMovie().getName().toLowerCase().contains(
                                        searchName))
                                        .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public void returnMovie(Rental rental, LocalDate returnDate){
        rental.setReturnDate(returnDate);
        if (returnDate.isAfter(rental.getEndDate())){
            rental.setLateFee(20);
            rental.setPaidFee(false);
        }
    }
    
    public void payLateFee(Rental rental){
        rental.setPaidFee(true);
    }
    
    public boolean isLateFeePaid(Rental rental){
        return rental.isPaidFee();
    }
    
    
}
