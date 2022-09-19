package models;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private String title;
    private int releaseYear;
    private List<String> genres;
    private int runningTime;
    private String productionCompany;
    private long budget;
    private long revenue;

    public Movie(String title, int releaseYear, List<String> genres, int runningTime, String productionCompany, long budget, long revenue) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.runningTime = runningTime;
        this.productionCompany = productionCompany;
        this.budget = budget;
        this.revenue = revenue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getProfit(){
        return revenue - budget;
    }

    public void display(){
        System.out.println("Movie Title: " + title );
        System.out.println("Year of Release: " + releaseYear );
        System.out.print("Genre: ");
        for(int i=0;i<genres.size()-1;i++){
            System.out.print(genres.get(i) + ", ");
        }
        System.out.println(genres.get(genres.size()-1));
        System.out.println("Running Time (in minutes): " + runningTime );
        System.out.println("Production Company: " + productionCompany );
        System.out.println("Budget: " + budget );
        System.out.println("Revenue: " + revenue );
        System.out.println("");
    }


}
