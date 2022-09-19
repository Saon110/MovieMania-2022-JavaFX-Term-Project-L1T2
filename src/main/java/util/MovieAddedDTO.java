package util;

import models.Movie;
import models.ProductionCompany;

import java.io.Serializable;

public class MovieAddedDTO implements Serializable {
    private Movie m;
    private String from;
    private int demo;
    private ProductionCompany p;

    public MovieAddedDTO(ProductionCompany p,Movie m, String from) {
        this.m = m;
        this.from = from;
        this.p = p;
    }

    public ProductionCompany getP() {
        return p;
    }

    public void setP(ProductionCompany p) {
        this.p = p;
    }

    public Movie getM() {
        return m;
    }

    public void setM(Movie m) {
        this.m = m;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
