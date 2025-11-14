package application.application.services;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import application.domain.entities.movie.Movie;
import application.domain.entities.rental.Rental;
import org.springframework.stereotype.Service;
import application.repositories.inmemory.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movies;

    public MovieService(MovieRepository movies) {
        this.movies = Objects.requireNonNull(movies, "MovieRepository cannot be null");
    }

    public Movie save(Movie movie) {
        Objects.requireNonNull(movie, "Movie cannot be null");
        return movies.save(movie);
    }

    public Movie findById(int id) {
        return findMovieOrFail(id);
    }

    public ArrayList<Movie> findByName(String name) {
        if (isNullOrBlank(name)) {
            return new ArrayList<>();
        }

        String searchName = normalizeName(name);
        return movies.findAll().stream()
                .filter(movie -> containsName(movie, searchName))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void rentMovie(Movie movie) {
        Objects.requireNonNull(movie, "Movie cannot be null");

        Movie existingMovie = findMovieOrFail(movie.getId());
        ensureMovieIsAvailable(existingMovie);

        existingMovie.setAvailable(false);
        movies.save(existingMovie);
    }

    private Movie findMovieOrFail(int id) {
        return movies.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
    }

    private void ensureMovieIsAvailable(Movie movie) {
        if (!movie.isAvailable()) {
            throw new IllegalStateException(
                    String.format("Movie with id %d is already rented", movie.getId())
            );
        }
    }

    private boolean isNullOrBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeName(String name) {
        return name.toLowerCase().trim();
    }

    private boolean containsName(Movie movie, String searchName) {
        return movie.getName().toLowerCase().contains(searchName);
    }

    public ArrayList<Movie> findAll() {
        return new ArrayList<>(movies.findAll());
    }

//    public Rental returnMovie(Movie movie) {
//        Objects.requireNonNull(movie, "Movie cannot be null");
//        
//        Movie existingMovie = findMovieOrFail(movie.getId());
//        
//        existingMovie.setAvailable(true);
//        movies.save(existingMovie);
//        
//        return new Rental(existingMovie);
//    }
}
