package application.services;

import java.util.Objects;
import repositories.inmemory.MovieRepository;
import domain.entities.movie.Movie;
import domain.entities.movie.Rental;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MovieService {

    private final MovieRepository movies;

    public MovieService(MovieRepository movies) {
        this.movies = Objects.requireNonNull(movies, "MovieRepository cannot be null");
    }
    
    public Movie save(Movie movie){
        Objects.requireNonNull(movie, "Movie cannot be null");
        return movies.save(movie);
    }

    public Movie findById(int id) {
        return movies.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + id));
    }

    public ArrayList<Movie> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchName = name.toLowerCase().trim();
        return movies.findAll().stream()
                .filter(movie -> movie.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void rentMovie(Movie movie) {
        Objects.requireNonNull(movie, "Movie cannot be null");
        
        Movie existingMovie = findById(movie.getId());
        
        if (!existingMovie.isAvailable()) {
            throw new IllegalStateException("Movie is already rented");
        }
        
        existingMovie.setAvailable(false);
        movies.save(existingMovie);
    }

//    public Rental returnMovie(Movie movie) {
//        Objects.requireNonNull(movie, "Movie cannot be null");
//        
//        Movie existingMovie = findById(movie.getId());
//        
//        existingMovie.setAvailable(true);
//        movies.save(existingMovie);
//        
//        // Retorna um novo Rental (assumindo construtor que aceita Movie)
//        return new Rental(existingMovie);
//    }
}