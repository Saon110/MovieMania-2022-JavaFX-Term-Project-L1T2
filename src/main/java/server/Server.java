package server;

import models.Movie;
import models.MovieList;
import models.ProductionCompany;
import util.FileOperations;
import util.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private MovieList movieDataBase;
    private ConcurrentHashMap<String,String> passwordList;
    private List<String>companyNamesList;
    private ConcurrentHashMap<String,NetworkUtil> companyNetworkMap;
    private ConcurrentHashMap<String,String> trailerList;

    public ConcurrentHashMap<String, String> getTrailerList() {
        return trailerList;
    }

    ServerSocket serverSocket;
    private volatile int clientCount;

    private volatile int updateCount;

    public int getClientCount() {
        return clientCount;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public synchronized void incrementClientCount(){
        clientCount++;
    }
    public synchronized void decrementClientCount(){
        clientCount--;
    }
    public synchronized void incrementUpdateCount(){
        updateCount++;
    }

    public ConcurrentHashMap<String, NetworkUtil> getCompanyNetworkMap() {
        return companyNetworkMap;
    }

    public List<String> getCompanyNamesList() {
        return companyNamesList;
    }

    public MovieList getMovieDataBase() {
        return movieDataBase;
    }

    public ConcurrentHashMap<String, String> getPasswordList() {
        return passwordList;
    }


    Server() throws IOException {

        clientCount = 0;
        updateCount = 0;
        List<Movie> extractedMovies = FileOperations.readMoviesFromFile();
        movieDataBase = new MovieList(extractedMovies);
        System.out.println("Movie Database successfully loaded...");
        passwordList = FileOperations.readPasswordsFromFile();
        System.out.println("Credentials List successfully loaded...");
        trailerList = FileOperations.readTrailersFromFile();
        System.out.println("Trailers List successfully loaded...");

        companyNamesList = new ArrayList<>();

        for(Movie m: movieDataBase.getMovieList()){
            companyNamesList.add(m.getProductionCompany());
        }

        companyNetworkMap = new ConcurrentHashMap<>();

        new FileWriteThreadServer(this);
        new InputThreadServer(this);

        //FileOperations.writeAllMoviesToFile(movieDataBase);
        //FileOperations.writePasswordsToFile(passwordList);

        try{
            serverSocket = new ServerSocket(44444);
            System.out.println("Server is waiting...");
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server accepts a client...");
                var networkUtil = new NetworkUtil(clientSocket);
                new ReadThreadServer(clientSocket, this, networkUtil);
            }

        } catch(Exception e){
            System.out.println("Server starts" + e);

        }
    }

    public static void main(String[] args) throws IOException{
        Server s = new Server();
    }
}


