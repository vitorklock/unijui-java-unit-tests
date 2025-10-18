/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.application.service;

import application.services.MovieService;
import shared.repository.InMemoryRepository;
import domain.entities.movie.Movie;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.inmemory.MovieRepository;

class MovieRepositorySpy extends MovieRepository {
}

public class TestMovieService {

    private MovieRepository repo;
    private MovieService service;
    private Movie movie;

    @BeforeEach
    public void setUpRepo() {
        repo = new MovieRepositorySpy();
        service = new MovieService(repo);
        movie = new Movie("Barbie Movie");
    }

    @Test
    public void mustSaveMovie() {

        service.save(movie);

        Optional<Movie> optionalSavedMovie = repo.findById(movie.getId());

        Assertions.assertTrue(optionalSavedMovie.isPresent());

        Movie savedMovie = optionalSavedMovie.get();

        Assertions.assertNotNull(savedMovie);
        Assertions.assertEquals(movie.getId(), savedMovie);
    }

    @Test
    public void mustFindMovieById() {

        repo.save(movie);

        Movie savedMovie = Assertions.assertDoesNotThrow(() -> service.findById(movie.getId()));

        Assertions.assertNotNull(savedMovie);
    }

    @Test
    public void mustFindMovieByName() {

        repo.save(movie);

        ArrayList<Movie> foundMovies = service.findByName(movie.getName());

        Assertions.assertTrue(foundMovies.contains(movie));
    }

    @Test
    public void mustNotBeAvailableAfterBeingRented() {

        service.save(movie);

        service.rentMovie(movie);

        Assertions.assertFalse(movie.isAvailable());
    }

    public void mustNotRentAlreadyRentedMovie() {

        service.save(movie);

        service.rentMovie(movie);
        Assertions.assertFalse(movie.isAvailable());

        IllegalStateException ex = Assertions.assertThrows(
                IllegalStateException.class,
                () -> service.rentMovie(movie)
        );

    }

}
