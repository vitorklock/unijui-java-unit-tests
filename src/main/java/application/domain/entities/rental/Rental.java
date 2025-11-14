package application.domain.entities.rental;

import application.domain.entities.movie.Movie;
import java.time.LocalDate;

// Deve ter:
// filme sendo alugado
// inicio do aluguel (data de retirada)
// fim do aluguel 
// data de retorno
// multa (valor reais)
// multa paga (booleano)

public class Rental {
    private static int nextId = 1;
    private int id;
    private Movie rentedMovie;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate returnDate;
    private double lateFee;
    private boolean paidFee;
    
    public Rental (Movie rentedMovie){
        this.id = nextId++;
        this.rentedMovie = rentedMovie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getRentedMovie() {
        return rentedMovie;
    }

    public void setRentedMovie(Movie rentedMovie) {
        this.rentedMovie = rentedMovie;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double fee) {
        this.lateFee = fee;
    }

    public boolean isPaidFee() {
        return paidFee;
    }

    public void setPaidFee(boolean paidFee) {
        this.paidFee = paidFee;
    }
    public void updateReturn(LocalDate newReturnDate){
        setReturnDate(newReturnDate);
        setLateFee(calculateLateFee(getEndDate(), newReturnDate));
        setPaidFee(false);
        
    }
    
    public void updateRentalDates(){
        LocalDate today = LocalDate.now();
        setStartDate(today);
        setEndDate(today.plusDays(7));
    }
    private double calculateLateFee(LocalDate endDate, LocalDate returndate){
        //fixed late fee of 20 for the time being
        //in the future might be calculated based on how many days the movie is late
        double newLateFee = 0;
        if (returndate.isAfter(endDate)){
            newLateFee = 20; }
        return newLateFee;
    }
    
}
