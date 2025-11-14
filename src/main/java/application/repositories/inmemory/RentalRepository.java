
package application.repositories.inmemory;

import application.domain.entities.rental.Rental;
import java.util.function.ToIntFunction;
import org.springframework.stereotype.Repository;
import application.shared.repository.InMemoryRepository;

@Repository
public class RentalRepository extends InMemoryRepository<Rental>{
    
    public RentalRepository() {
        super(Rental::getId);
    }
    
}
