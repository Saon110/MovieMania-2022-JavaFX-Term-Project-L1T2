package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Movie;
import models.ProductionCompany;
import client.Main;
import util.AddMovieDTO;
import util.LogoutDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddMovieController {

    private Main main;

    @FXML
    private Label message;


    @FXML
    private Button button;
    @FXML
    private Button recentMoviesBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button maxRevBtn;
    @FXML
    private Button addMovieBtn;

    @FXML
    private TextField budgetInput;

    @FXML
    private Button doAddBtn;

    @FXML
    private TextField genreInput;


    @FXML
    private TextField revenueInput;

    @FXML
    private TextField timeInput;

    @FXML
    private TextField titleInput;

    @FXML
    private TextField yearInput;
    @FXML
    private Label companyName;

    @FXML
    private Button transferMovieBtn;
    @FXML
    private Button passChangeBtn;
    @FXML
    private Button searchBtn;


    public void init(ProductionCompany p) {
        companyName.setText("  " + p.getName());
        message.setText(p.getName());
        hightlight(addMovieBtn);
        homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showHomePage(p,"Home","");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        passChangeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showChangePassPage(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        recentMoviesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showHomePage(p,"Recent","");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        maxRevBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showHomePage(p,"Max","");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        transferMovieBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showTransferPage(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showSearchMoviePage(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        doAddBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String title;
                int releaseYear;
                List<String> genres = new ArrayList();
                int runningTime;
                String productionCompany;
                int budget;
                int revenue;

                title = titleInput.getText().strip();
                releaseYear = parseToInt(yearInput.getText().strip());
                runningTime = parseToInt(timeInput.getText().strip());
                productionCompany = p.getName();
                budget = parseToInt(budgetInput.getText().strip());
                revenue = parseToInt(revenueInput.getText().strip());

                String[] g = genreInput.getText().split(",");
                if(g.length<3){
                    int i=0;
                    for(String s: g){
                        genres.add(s);
                        i++;
                        if(i==3)
                            break;
                    }
                }
                else{
                    for(int i=0;i<3;i++){
                        genres.add(g[i]);
                    }
                }

                Movie m = new Movie(title,releaseYear,genres,runningTime,productionCompany,budget,revenue);

                m.display();

                if(notValid(title) || releaseYear==-1 || runningTime==-1 || budget==-1 || revenue==-1){
                    main.showAlert("Invalid Input","Invalid Inpput","Please input carefully");
                }
                else{
                    var aDTO = new AddMovieDTO(p,m);
                    try {
                        main.getNetworkUtil().write(aDTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //p.addMovie(m);
                    /*
                    try {
                        main.showHomePage(p,"Home","");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                     */
                    //main.showAlert("Success","Success","Movie Addedd Successfully");
                }


            }
        });


    }

    public boolean notValid(String s){  // String with comma will not be allowed
        if(s.contains(",")){
            return true;
        }
        else {
            return false;
        }
    }

    public int parseToInt(String s){
        int n;
        try{
            n = Integer.parseInt(s);
        } catch(NumberFormatException e){
            n = -1;
        }
        return n;
    }

    public void unHighlightAllMenu(){
        unHightlight(homeBtn);
        unHightlight(recentMoviesBtn);
        unHightlight(maxRevBtn);
        unHightlight(addMovieBtn);
    }
    public void hightlight(Button btn){
        btn.setStyle("-fx-background-color: #90ee90 ; -fx-background-radius: 20;");
    }

    public void unHightlight(Button btn){
        btn.setStyle("-fx-background-color: white ; -fx-background-radius: 20;");
    }


    @FXML
    public void logoutAction(ActionEvent event) {
        var lDTO = new LogoutDTO(main.getCompanyName());
        lDTO.setStatus(true);
        try {
            main.getNetworkUtil().write(lDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            main.showLoginPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
