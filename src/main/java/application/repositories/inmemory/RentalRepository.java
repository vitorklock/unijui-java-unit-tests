
package repositories.inmemory;

import domain.entities.movie.Rental;
import java.util.function.ToIntFunction;
import shared.repository.InMemoryRepository;


public class RentalRepository extends InMemoryRepository<Rental>{
    
    public RentalRepository() {
        super(Rental::getId);
    }
    
}
