/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.application.service;

import application.application.services.MovieService;
import application.shared.repository.InMemoryRepository;
import application.domain.entities.movie.Movie;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import application.repositories.inmemory.MovieRepository;
import static org.mockito.Mockito.*;



public class TestMovieService {

    private MovieRepository repo;
    private MovieService service;
    private Movie movie;

    @BeforeEach
    public void setUpRepo() {
        repo = mock(MovieRepository.class);
        service = new MovieService(repo);
        movie = mock(Movie.class);
        when(movie.getId()).thenReturn(1);
        when(movie.getName()).thenReturn("Barbie Movie");
    }

    @Test
    public void mustSaveMovie() {
        when(repo.save(movie)).thenReturn(movie);
        when(repo.findById(1)).thenReturn(Optional.of(movie));

        service.save(movie);

        Optional<Movie> optionalSavedMovie = repo.findById(movie.getId());

        Assertions.assertTrue(optionalSavedMovie.isPresent());

        Movie savedMovie = optionalSavedMovie.get();

        Assertions.assertNotNull(savedMovie);
        Assertions.assertEquals(movie.getId(), savedMovie.getId());
        
        verify(repo).save(movie);
    }

    @Test
    public void mustFindMovieById() {
        when(repo.findById(1)).thenReturn(Optional.of(movie));

        Movie savedMovie = Assertions.assertDoesNotThrow(() -> service.findById(1));

        Assertions.assertNotNull(savedMovie);
        Assertions.assertEquals(1, savedMovie.getId());
        
        verify(repo).findById(1);
    }

    @Test
    public void mustFindMovieByName() {
        ArrayList<Movie> mockList = new ArrayList<>();
        mockList.add(movie);
        when(repo.findAll()).thenReturn(mockList);

        ArrayList<Movie> foundMovies = service.findByName("Barbie Movie");

        Assertions.assertTrue(foundMovies.contains(movie));
        
        verify(repo).findAll();
    }

    @Test
    public void mustNotBeAvailableAfterBeingRented() {
        when(repo.save(movie)).thenReturn(movie);
        when(repo.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(movie.isAvailable()).thenReturn(true);

        service.rentMovie(movie);


        verify(movie).setAvailable(false);
        verify(repo).save(movie);
    }
    
    @Test
    public void mustNotRentAlreadyRentedMovie() {
        when(repo.save(movie)).thenReturn(movie);
        when(repo.findById(movie.getId())).thenReturn(Optional.of(movie));
        when(movie.isAvailable()).thenReturn(false);

        IllegalStateException ex = Assertions.assertThrows(
                IllegalStateException.class,
                () -> service.rentMovie(movie)
        );

    }

}
