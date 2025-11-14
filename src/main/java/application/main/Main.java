package application.main;

import application.application.services.MovieService;
import application.application.services.RentalService;
import application.domain.entities.movie.Movie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "application")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(MovieService movieService, RentalService rentalService) {
        return args -> {
            // Create initial movies
            Movie garfield = movieService.save(new Movie("Garfield"));
            Movie dune = movieService.save(new Movie("Dune"));
            Movie matrix = movieService.save(new Movie("Matrix"));

            // Rent "Matrix" 
            rentalService.createRentalForMovie(matrix.getId());

            System.out.println("Seeded movies: Garfield, Dune, Matrix (Matrix is rented).");
        };
    }
}
