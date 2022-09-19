package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MovieList implements Serializable {

    private List<Movie> movieList;

    public MovieList(){
        this.movieList = new ArrayList();
    }

    public MovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList(){
        return movieList;
    }

    public int getMovieCount(){
        if(movieList == null)
            return 0;
        else
            return movieList.size();
    }

    public synchronized boolean addMovie(Movie m){
        boolean ok = true;
        for(Movie m1: movieList){
            if(m1.getTitle().equalsIgnoreCase(m.getTitle())){
                ok = false;
                break;
            }
        }
        if(ok) {
            movieList.add(m);
        }
        return ok;
    }

    public synchronized boolean addMovie(int index,Movie m){
        boolean ok = true;
        for(Movie m1: movieList){
            if(m1.getTitle().equalsIgnoreCase(m.getTitle())){
                ok = false;
                break;
            }
        }
        if(ok) {
            movieList.add(index,m);
        }
        return ok;
    }

    public void display(){

        int i = 1;

        for(Movie m: movieList){
            System.out.println(i + ".");
            System.out.println("");
            m.display();
            i++;
        }

    }

    public MovieList searchByTitle(String t){

        List<Movie>foundMovies = new ArrayList();

        for(Movie m: movieList) {
            if(m.getTitle().equalsIgnoreCase(t)){
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }
    }

    public MovieList searchByReleaseYear(int y){

        List<Movie>foundMovies = new ArrayList();

        for(Movie m: movieList) {
            if(m.getReleaseYear() == y){
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }
    }

    public MovieList searchByGenre(String g){

        List<Movie>foundMovies = new ArrayList();

        for(Movie m: movieList) {
            int mark = 0;
            for(String s: m.getGenres()){
                if(s.equalsIgnoreCase(g)){
                    mark = 1;
                    break;
                }
            }
            if(mark==1){
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }
    }

    public MovieList searchByProductionCompany(String p){

        List<Movie>foundMovies = new ArrayList();

        for(Movie m: movieList) {
            if(m.getProductionCompany().equalsIgnoreCase(p)){
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }
    }

    public MovieList searchByRunningTime(int lowerBound, int upperBound){

        List<Movie>foundMovies = new ArrayList();

        for(Movie m: movieList) {
            int t = m.getRunningTime();
            if(t>=lowerBound && t<=upperBound){
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }
    }

    public MovieList topTenByProfit(){

        List<Movie>foundMovies = new ArrayList();

        int[] indicesOfSortedMovies = new int[10];

        for(int i=0;i<10;i++){ // i refers to i'th movie after sorting in descending order of profit
            long nowMaxProfit = -Long.MAX_VALUE;
            int nowIndex = -1;
            for(int j=0;j<movieList.size();j++){
                if(movieList.get(j).getProfit() > nowMaxProfit){
                    int mark = 1;
                    for(int k=0;k<i;k++){
                        if(indicesOfSortedMovies[k] == j){
                            mark = 0;
                        }
                    }
                    if(mark==1){
                        nowMaxProfit = movieList.get(j).getProfit();
                        nowIndex = j;
                    }
                }
            }
            foundMovies.add(movieList.get(nowIndex));
            indicesOfSortedMovies[i] = nowIndex;

        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }
    }

    public MovieList searchByCompanyLatest(String p){
        List<Movie>foundMovies = new ArrayList();

        MovieList companyMovies = searchByProductionCompany(p);

        if(companyMovies == null){
            return null;
        }

        int latestReleaseYear = 0;
        for(Movie m: companyMovies.getMovieList()){
            if(m.getReleaseYear()>latestReleaseYear)
            {
                latestReleaseYear = m.getReleaseYear();
            }
        }

        for(Movie m: companyMovies.getMovieList()){
            if(m.getReleaseYear() == latestReleaseYear)
            {
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }

    }

    public MovieList searchByCompanyMaxRevenue(String p){
        List<Movie>foundMovies = new ArrayList();

        MovieList companyMovies = searchByProductionCompany(p);

        if(companyMovies == null){
            return null;  // No Production Company of this name
        }

        long maxRevenue = -Long.MAX_VALUE;
        for(Movie m: companyMovies.getMovieList()){
            if(m.getRevenue()>maxRevenue)
            {
                maxRevenue = m.getRevenue();
            }
        }

        for(Movie m: companyMovies.getMovieList()){
            if(m.getRevenue() == maxRevenue)
            {
                foundMovies.add(m);
            }
        }

        if(foundMovies.size() == 0){
            return null;
        }
        else{
            MovieList movies = new MovieList(foundMovies);
            return movies;
        }

    }

    public long searchByCompanyTotalProfit(String p){

        MovieList companyMovies = searchByProductionCompany(p);

        if(companyMovies == null){
            return -1;
        }

        long totalProfit = 0;

        for(Movie m: companyMovies.getMovieList()){
            totalProfit  = totalProfit + m.getProfit();
        }

        return totalProfit;

    }

    public List<ProductionCompany> getAllCompanies(){
        List<ProductionCompany>companies = new ArrayList();

        List<String>companyNames = new ArrayList();


        int ii = -1;
        for(Movie m: movieList){
            ii++;
            String name = m.getProductionCompany();

            if(companyNames.contains(name)){
                int index = companyNames.indexOf(name);
                companies.get(index).increaseMovieCount();
            }
            else{
                if(name.length()>0) {
                    companyNames.add(name);
                    ProductionCompany temp = new ProductionCompany(name, 1);
                    companies.add(temp);
                }
            }
        }

        return companies;

    }

    public synchronized void updateProductionCompany(String title, String newCompany){
        Movie m = this.searchByTitle(title).getMovieList().get(0);
        m.setProductionCompany(newCompany);
        System.out.println("Production Company of " + m.getTitle() + " updated");
    }

    public static void main(String[] args) {

    }
}

