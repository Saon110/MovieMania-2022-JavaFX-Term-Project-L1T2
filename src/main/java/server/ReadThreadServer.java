package server;

import models.Movie;
import models.MovieList;
import models.ProductionCompany;
import util.*;
import util.NetworkUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReadThreadServer implements Runnable{

    NetworkUtil networkUtil;
    Server server;
    Socket clientSocket;
    Thread t;
    private boolean threadOn = true;
    MovieList movieDataBase;
    ConcurrentHashMap<String,String> passwordList;
    ConcurrentHashMap<String,String> trailerList;
    List<String>companyNamesList;
    ConcurrentHashMap<String,NetworkUtil> companyNetworkMap;

    ReadThreadServer(Socket clientSocket, Server server, NetworkUtil networkUtil){
        this.clientSocket = clientSocket;
        this.server = server;
        this.networkUtil = networkUtil;
        this.movieDataBase = server.getMovieDataBase();
        this.passwordList = server.getPasswordList();
        this.companyNamesList = server.getCompanyNamesList();
        this.companyNetworkMap = server.getCompanyNetworkMap();
        this.trailerList = server.getTrailerList();
        t = new Thread(this);
        t.start();
    }

    public boolean isValidCompany(String name){
        boolean ans = false;
        for(Movie m: movieDataBase.getMovieList()){
            if(m.getProductionCompany().equalsIgnoreCase(name)){
                ans = true;
            }
        }
        return ans;
    }

    public String validCompany(String name){
        String ans="";
        for(Movie m: movieDataBase.getMovieList()){
            if(m.getProductionCompany().equalsIgnoreCase(name)){
                ans = m.getProductionCompany();
                return ans;
            }
        }
        return ans;
    }

    public void run() {
        while (threadOn) {
            Object next = null;
            while (threadOn) {
                try {
                    next = networkUtil.read();
                    break;
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
            if (next instanceof String) {
                System.out.println("Received String  "+next);
            }
            if (next instanceof LoginDTO) {
                LoginDTO loginDTO = (LoginDTO) next;
                String name = loginDTO.getUserName();

                boolean isAuth;

                MovieList mm = movieDataBase.searchByProductionCompany(name);
                if(mm==null){
                    System.out.println("No such Company in my Database");
                    isAuth = false;
                }
                else {
                    name = mm.getMovieList().get(0).getProductionCompany();
                    String password = passwordList.get(name);
                    isAuth = loginDTO.getPassword().equals(password);
                }
                loginDTO.setStatus(isAuth);
                try {
                    networkUtil.write(loginDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(isAuth){
                    server.incrementClientCount();
                    companyNetworkMap.put(name,networkUtil);
                    System.out.println(name + " successfully connects to server...");
                    MovieList m = movieDataBase.searchByProductionCompany(name);
                    long profit = movieDataBase.searchByCompanyTotalProfit(name);
                    //m.display();
                    var p = new ProductionCompany(name,m);
                    p.setTotalProfit(profit);
                    //p.getCompanyMovies().display();
                    var pDTO = new ProductionCompanyDTO();

                    ConcurrentHashMap<String,String>map = new ConcurrentHashMap<>();
                    for(Movie m1: p.getCompanyMovies().getMovieList()){
                        String title = m1.getTitle();
                        //System.out.println(trailerList.get(title));
                        if(trailerList.get(title)==null){
                            map.put(title,"dummy");
                        }
                        else {
                            map.put(title, trailerList.get(title));
                        }
                    }

                    p.setTrailerList(map);

                    pDTO.setCompany(p);

                    //pDTO.getCompany().getCompanyMovies().display();
                    try {
                        networkUtil.write(pDTO);
                        System.out.println("Company details successfully sent to " + name);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println();
            }
            if (next instanceof TransferDTO) {

                System.out.println("Transfer request received");

                var tDTO = (TransferDTO) next;

                String from = tDTO.getFromCompany();
                String to = tDTO.getToCompany();
                Movie m = tDTO.getMovie();

                String name = m.getTitle();

                System.out.println(from + " requested to transfer " + m.getTitle() + " to " + to);

                if(isValidCompany(to)){
                    server.incrementUpdateCount();
                    to = validCompany(to);
                    System.out.println("Company Name is valid, I am transferring...");

                    movieDataBase.updateProductionCompany(name,to);

                    tDTO.setStatus(true);

                    System.out.println(tDTO.getCompany().getName());

                    ProductionCompany pp = tDTO.getCompany();
                    pp.removeMovie(m);
                    tDTO.setCompany(pp);

                    try {
                        networkUtil.write(tDTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(companyNetworkMap.containsKey(to)){
                        MovieList mm = new MovieList();
                        mm.addMovie(m);
                        MovieList temp = movieDataBase.searchByProductionCompany(to);
                        for(Movie m1: temp.getMovieList()){
                            if(!(m1.getTitle().equals(m.getTitle()))){
                                mm.addMovie(m1);
                            }
                        }
                        var p = new ProductionCompany(to,mm);

                        ConcurrentHashMap<String,String>map = new ConcurrentHashMap<>();
                        for(Movie m1: p.getCompanyMovies().getMovieList()){
                            String title = m1.getTitle();
                            if(trailerList.get(title)==null){
                                map.put(title,"dummy");
                            }
                            else {
                                map.put(title, trailerList.get(title));
                            }
                        }

                        p.setTrailerList(map);

                        var mDTO = new MovieAddedDTO(p,m,from);
                        var toNetworkUtil = companyNetworkMap.get(to);
                        try {
                            toNetworkUtil.write(mDTO);
                        } catch (IOException e) {
                            //e.printStackTrace();
                            System.out.println("Notification cannot be sent to " + to + " because of abrupt closure of connection.");
                        }
                    }


                }
                else{
                    System.out.println("No such Company Exixts in My Database");
                    tDTO.setStatus(false);
                    try {
                        networkUtil.write(tDTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            if(next instanceof AddMovieDTO){
                var aDTO = (AddMovieDTO) next;
                ProductionCompany p = aDTO.getCompany();
                Movie m = aDTO.getMovie();
                System.out.println("Add Movie request received for " + m.getTitle());
                boolean added = movieDataBase.addMovie(m);
                if(added) {
                    server.incrementUpdateCount();
                    System.out.println("Movie successsfuly added");
                    aDTO.setStatus(true);
                    //p.addMovie(m);
                    System.out.println("First Movie " + p.getCompanyMovies().getMovieList().get(0).getTitle());
                    System.out.println("Profit at sent company "+p.getTotalProfit());
                    ConcurrentHashMap<String,String>map = new ConcurrentHashMap<>();
                    for(Movie m1: p.getCompanyMovies().getMovieList()){
                        String title = m1.getTitle();
                        //System.out.println(trailerList.get(title));
                        if(trailerList.get(title)==null){
                            map.put(title,"dummy");
                        }
                        else {
                            map.put(title, trailerList.get(title));
                        }
                    }

                    p.setTrailerList(map);
                    aDTO.setCompany(p);
                    System.out.println("In sent DTO");
                    System.out.println(aDTO.getCompany().getCompanyMovies().getMovieList().get(0).getTitle());

                }
                else{
                    System.out.println("Movie name not unique");
                    aDTO.setStatus(false);
                }
                try {
                    networkUtil.write(aDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(next instanceof PasswordChangeDTO){
                var pDTO = (PasswordChangeDTO) next;

                String name = pDTO.getCompanyName();
                String currentPass = pDTO.getCurrentPass();
                String newPass = pDTO.getNewPass();

                System.out.println("Password Change request received");

                if(!(passwordList.get(name).equals(currentPass))){
                    pDTO.setStatus(false);
                }
                else{
                    pDTO.setStatus(true);
                    passwordList.put(name,newPass);
                    server.incrementUpdateCount();
                    System.out.println("Password successfully changed in server database.");
                }

                try {
                    networkUtil.write(pDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if(next instanceof LogoutDTO){
                server.decrementClientCount();
                var lDTO = (LogoutDTO) next;
                System.out.println(lDTO.getCompanyName() + " logs out...");
                companyNetworkMap.remove(lDTO.getCompanyName());
            }
        }

    }
}
