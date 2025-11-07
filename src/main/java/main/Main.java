package main;

import application.services.MovieService;
import domain.entities.movie.Movie;
import repositories.inmemory.MovieRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE LOCADORA DE FILMES ===\n");
        
        // Inicialização
        MovieRepository repository = new MovieRepository();
        MovieService service = new MovieService(repository);
        
        // Criando e salvando filmes
        System.out.println("1. Cadastrando filmes...");
        Movie movie1 = service.save(new Movie("Clube da Luta"));
        Movie movie2 = service.save(new Movie("O Poderoso Chefão"));
        Movie movie3 = service.save(new Movie("Matrix"));
        
        System.out.println("   - " + movie1.getName() + " (ID: " + movie1.getId() + ")");
        System.out.println("   - " + movie2.getName() + " (ID: " + movie2.getId() + ")");
        System.out.println("   - " + movie3.getName() + " (ID: " + movie3.getId() + ")");
        
        // Buscando por ID
        System.out.println("\n2. Buscando filme pelo ID " + movie2.getId() + "...");
        Movie foundById = service.findById(movie2.getId());
        System.out.println("   - Encontrado: " + foundById.getName());
        
        // Buscando por nome
        System.out.println("\n3. Buscando filmes com 'Chefão' no nome...");
        var foundByName = service.findByName("Chefão");
        System.out.println("   - Quantidade: " + foundByName.size());
        foundByName.forEach(m -> 
            System.out.println("     > " + m.getName() + " (ID: " + m.getId() + ")")
        );
        
        // Alugando um filme
        System.out.println("\n4. Alugando: " + movie1.getName());
        System.out.println("   - Antes: " + movie1.isAvailable());
        service.rentMovie(movie1);
        System.out.println("   - Depois: " + service.findById(movie1.getId()).isAvailable());
        
        // Tentando alugar novamente
        System.out.println("\n5. Tentando alugar o mesmo filme...");
        try {
            service.rentMovie(movie1);
            System.out.println("   - ERRO: Não deveria permitir!");
        } catch (IllegalStateException e) {
            System.out.println("   - ✅ Exceção esperada: " + e.getMessage());
        }
        
        // Status final
        System.out.println("\n6. Status final do acervo:");
        System.out.println("   - " + movie1.getName() + ": " + 
                         (service.findById(movie1.getId()).isAvailable() ? "Disponível" : "Alugado"));
        System.out.println("   - " + movie2.getName() + ": " + 
                         (service.findById(movie2.getId()).isAvailable() ? "Disponível" : "Alugado"));
        System.out.println("   - " + movie3.getName() + ": " + 
                         (service.findById(movie3.getId()).isAvailable() ? "Disponível" : "Alugado"));
        
        System.out.println("\n=== FIM DO TESTE ===");
    }
}