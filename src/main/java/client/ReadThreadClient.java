package client;

import javafx.application.Platform;
import models.Movie;
import models.ProductionCompany;
import util.*;

import java.io.IOException;

public class ReadThreadClient implements Runnable {
    private final Thread thr;
    private final Main main;

    public ReadThreadClient(Main main) {
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = main.getNetworkUtil().read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        System.out.println(loginDTO.getUserName());
                        System.out.println(loginDTO.isStatus());
                        // the following is necessary to update JavaFX UI components from user created threads
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (loginDTO.isStatus()) {
                                    System.out.println("Login Successful");
                                    /*try {
                                        main.showHomePage(loginDTO.getUserName());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }*/
                                } else {
                                    main.showAlert("Incorrect Credentials","Incorrect Credentials","Username and Password not Correct");
                                }

                            }
                        });
                    }
                    if (o instanceof ProductionCompanyDTO){
                        System.out.println("Company Received");
                        var pDTO = (ProductionCompanyDTO) o;
                        var p = pDTO.getCompany();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    main.showHomePage(p,"Home","");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if (o instanceof TransferDTO){
                        System.out.println("TransferDTO Received");
                        var tDTO = (TransferDTO) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                ProductionCompany p = tDTO.getCompany();
                                if(tDTO.isStatus()){
                                    //main.showAlert("Tranfer successfull", "Tranfer successfull", "Successfully transferred the movie");
                                    try {
                                        main.showHomePage(p,"Home","One Movie Removed");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{
                                    main.showAlert("Incorrect Company","Incorrect Company","Sorry, No such company is found by Server");
                                }
                            }
                        });
                    }
                    if(o instanceof MovieAddedDTO){
                        System.out.println("Movie Added to Your company");
                        var mDTO = (MovieAddedDTO) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //main.showAlert("Movie Added","Movie Added", "Movie added to your company from " + mDTO.getFrom());
                                try {
                                    main.showHomePage(mDTO.getP(),"Home","New Movie Transferred From " + mDTO.getFrom());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    if(o instanceof PasswordChangeDTO){

                        var pDTO = (PasswordChangeDTO) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(pDTO.isStatus()){
                                    main.showAlert("Success","Success","Password changed successfully");
                                }
                                else{
                                    main.showAlert("Sorry","Sorry","Type current password carefully");
                                }
                            }
                        });
                    }
                    if(o instanceof AddMovieDTO) {

                        System.out.println("Add DTO Received");

                        var aDTO = (AddMovieDTO) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (aDTO.isStatus()) {
                                    System.out.println("New Movie received ");
                                    System.out.println(aDTO.getCompany().getCompanyMovies().getMovieList().get(0).getTitle());
                                    ProductionCompany p = aDTO.getCompany();
                                    Movie m = aDTO.getMovie();
                                    p.addMovie(m);
                                    p.getTrailerList().put(m.getTitle(),"dummy");
                                    try {
                                        main.showHomePage(p,"Home","New Movie Added");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    main.showAlert("Success", "Success", "Movie successfully Added");
                                } else {
                                    main.showAlert("Sorry", "Sorry", "Please provide a unique movie name.");
                                }
                            }
                        });
                    }
                    if(o instanceof StopDTO) {

                        var sDTO = (StopDTO) o;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (sDTO.isStatus()) {
                                    main.showAlert("Sorry", "Sorry", "Server Stopped Abruptly...");
                                    main.destroyClient();
                                } else {

                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                main.getNetworkUtil().closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



