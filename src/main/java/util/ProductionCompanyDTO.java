package util;

import models.ProductionCompany;

import java.io.Serializable;
import java.util.HashMap;

public class ProductionCompanyDTO implements Serializable {
    private HashMap<String,String> trailerList;
    private ProductionCompany company;

    public ProductionCompany getCompany() {
        return company;
    }

    public void setCompany(ProductionCompany company) {
        this.company = company;
    }

    public HashMap<String, String> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(HashMap<String, String> trailerList) {
        this.trailerList = trailerList;
    }
}
