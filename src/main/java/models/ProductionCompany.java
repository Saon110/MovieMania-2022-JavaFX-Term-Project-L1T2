package models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ProductionCompany implements Serializable {
    private String name;
    private int movieCount;
    private MovieList companyMovies;
    private long totalProfit;
    private ConcurrentHashMap<String,String> trailerList;

    public ConcurrentHashMap<String, String> getTrailerList() {
        return trailerList;
    }

    public void setTrailerList(ConcurrentHashMap<String, String> trailerList) {
        this.trailerList = trailerList;
    }

    public long getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(long totalProfit) {
        this.totalProfit = totalProfit;
    }

    public ProductionCompany(String name, MovieList companyMovies) {
        this.name = name;
        this.companyMovies = companyMovies;
        this.movieCount = companyMovies.getMovieCount();
        this.totalProfit = companyMovies.searchByCompanyTotalProfit(name);
    }

    public MovieList getCompanyMovies() {
        return companyMovies;
    }

    public ProductionCompany(String name) {
        this.name = name;
        this.movieCount = 0;
    }

    public ProductionCompany(String name, int movieCount) {
        this.name = name;
        this.movieCount = movieCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public void increaseMovieCount(){
        movieCount++;
    }

    public void display(){
        System.out.println("Production Company: " + name + ", Movies Produced: " + movieCount);
    }

    public synchronized void addMovie(Movie m){
        companyMovies.addMovie(0,m);
        movieCount++;
        totalProfit = totalProfit + m.getProfit();
        System.out.println("New Total Profit : " + totalProfit);
    }

    public synchronized void removeMovie(Movie m){
        int j = 0;
        for(int i=0;i<companyMovies.getMovieList().size();i++){
            if (m.getTitle().equals(companyMovies.getMovieList().get(i).getTitle())) {
                j = i;
            }
        }
        companyMovies.getMovieList().remove(j);
        movieCount--;
        totalProfit = totalProfit - m.getProfit();
    }
}
