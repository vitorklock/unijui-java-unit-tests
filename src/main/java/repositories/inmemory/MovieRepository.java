
package repositories.inmemory;

import shared.repository.InMemoryRepository;
import domain.entities.movie.Movie;

public class MovieRepository extends InMemoryRepository<Movie> {
    public MovieRepository() {
        super(Movie::getId);
    }
}
