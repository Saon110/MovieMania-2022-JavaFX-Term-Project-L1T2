package util;

import models.Movie;
import models.ProductionCompany;

import java.io.Serializable;

public class AddMovieDTO implements Serializable {
    ProductionCompany company;
    Movie movie;
    boolean status;



    public AddMovieDTO(ProductionCompany company, Movie movie) {
        this.company = company;
        this.movie = movie;
    }

    public ProductionCompany getCompany() {
        return company;
    }

    public void setCompany(ProductionCompany company) {
        this.company = company;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
