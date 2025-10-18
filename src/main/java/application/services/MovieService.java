package application.services;

import java.util.Objects;
import repositories.inmemory.MovieRepository;
import domain.entities.movie.Movie;
import domain.entities.movie.Rental;
import java.util.ArrayList;

public class MovieService {

    private final MovieRepository movies;

    public MovieService(MovieRepository movies) {
        this.movies = Objects.requireNonNull(movies);
    }
    
    public Movie save(Movie movie){
    }

    public Movie findById(int id) {
    }

    public ArrayList<Movie> findByName(String name) {
    }

    public void rentMovie(Movie movie) {
    }

    public Rental returnMovie(Movie movie) {
    }

}
