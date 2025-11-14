
package application.repositories.inmemory;

import application.shared.repository.InMemoryRepository;
import application.domain.entities.movie.Movie;
import org.springframework.stereotype.Repository;

@Repository
public class MovieRepository extends InMemoryRepository<Movie> {
    public MovieRepository() {
        super(Movie::getId);
    }
}
