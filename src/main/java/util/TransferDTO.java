package util;

import models.Movie;
import models.ProductionCompany;

import java.io.Serializable;

public class TransferDTO implements Serializable {
    private String fromCompany;
    private String toCompany;
    private Movie m;
    private boolean status;
    private ProductionCompany company;

    public TransferDTO(ProductionCompany p, String fromCompany, String toCompany, Movie m) {
        this.fromCompany = fromCompany;
        this.toCompany = toCompany;
        this.m = m;
        company = p;
        status = true;
    }

    public ProductionCompany getCompany() {
        return company;
    }

    public void setCompany(ProductionCompany company) {
        this.company = company;
    }

    public String getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(String fromCompany) {
        this.fromCompany = fromCompany;
    }

    public String getToCompany() {
        return toCompany;
    }

    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    public Movie getMovie() {
        return m;
    }

    public void setMovie(Movie m) {
        this.m = m;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
