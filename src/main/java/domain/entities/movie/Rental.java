/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain.entities.movie;

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
    
}
