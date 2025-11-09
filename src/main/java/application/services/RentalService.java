package application.services;

import domain.entities.movie.Movie;
import domain.entities.movie.Rental;
import java.time.LocalDate;
import java.util.ArrayList;
import repositories.inmemory.RentalRepository;


public class RentalService {
    private final RentalRepository rentals;
    
    public RentalService(RentalRepository rentals){
        this.rentals = rentals;
    }
    public Rental save(Rental rental) {
        return rental;
    }
    
    public Rental findById(int id){
        return null;
    }
    
    public ArrayList<Rental> findByMovieName(String movieName){
        ArrayList<Rental> placeholderArrayList = new ArrayList();  
        return placeholderArrayList;
    }
    
    public void returnMovie(Rental rental, LocalDate returnDate){
        
    }
    
    public void payLateFee(Rental rental){
        
    }
    
    public boolean isLateFeePaid(Rental rental){
        return false;
    }
    
    
}
