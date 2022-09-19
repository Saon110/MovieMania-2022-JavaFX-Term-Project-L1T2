package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Movie;
import models.MovieList;
import models.ProductionCompany;
import client.Main;
import util.LogoutDTO;
import util.TransferDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransferController {

    private Main main;

    @FXML
    private Label message;

    @FXML
    private ImageView image;

    @FXML
    private Button button;

    @FXML
    private ListView<Button> myList;
    @FXML
    private Label movieName;
    @FXML
    private Label listLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private Label countLabel;

    @FXML
    private Button recentMoviesBtn;
    @FXML
    private Button homeBtn;

    @FXML
    private Button maxRevBtn;
    @FXML
    private Button addMovieBtn;
    @FXML
    private Label movieDuration;

    @FXML
    private Label movieGenre;
    @FXML
    private Label movieYear;

    @FXML
    private Label movieBudget;
    @FXML
    private Label movieRevenue;
    @FXML
    private Button transferMovieBtn;

    @FXML
    private Label transferName;
    @FXML
    private TextField transferCompanyName;
    @FXML
    private Button doTransferBtn;
    @FXML
    private Button passChangeBtn;
    @FXML
    private ImageView moviePoster;
    List<String> dummies = new ArrayList<>();
    @FXML
    private Button searchBtn;


    public void init(ProductionCompany p, String listType, MovieList movieList) {
        message.setText(p.getName());
        //Image img = new Image(Main.class.getResourceAsStream("1.png"));
        //image.setImage(img);
        //listLabel.setText(listType);
        String profitText = "Total Profit: " + p.getTotalProfit();
        //profitLabel.setText(profitText);
        String countText = "Total Movies: " + p.getMovieCount();
        //countLabel.setText(countText);
        unsetMovieDetails();
        hightlight(transferMovieBtn);
        buildListView(p.getCompanyMovies());

        for(String s: p.getTrailerList().keySet()){
            if(p.getTrailerList().get(s).equals("dummy")){
                dummies.add(s);
            }
        }

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
        addMovieBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    main.showAddMoviePage(p);
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
        doTransferBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String str = transferName.getText();
                if(str.length()==0 || str.equals("Please Select A Movie")){
                    return;
                }
                String fromCompany = p.getName();
                String toCompany = transferCompanyName.getText().strip();
                if(toCompany.length()==0){
                    return;
                }
                Movie m = movieList.searchByTitle(str).getMovieList().get(0);
                var tDTO = new TransferDTO(p,fromCompany,toCompany,m);


                try {
                    main.getNetworkUtil().write(tDTO);
                    System.out.println("Transfer request sent to server");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void unsetMovieDetails(){
        movieName.setText("");
        movieGenre.setText("");
        movieDuration.setText("");
        movieYear.setText("");
        movieBudget.setText("");
        movieRevenue.setText("");
    }

    public void setMovieDetails(Movie m){

        String name = m.getTitle();
        String path = "";
        for(int i=0;i<name.length();i++){
            char ch = name.charAt(i);
            if((ch>=48 && ch<=57) || (ch>=64 && ch<=122)){
                path = path + name.charAt(i);
            }
        }
        path = "src/main/resources/images/" + path + "poster.jpg";
        if(dummies.contains(m.getTitle())){
            path = "src/main/resources/images/dummy.jpg";
        }



        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        moviePoster.setImage(img);



        moviePoster.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 10);");


        String genreText = "";
        int n = m.getGenres().size();
        for (int i=0;i<n-1;i++) {
            genreText = genreText + m.getGenres().get(i) + " | ";
        }
        genreText = genreText + m.getGenres().get(n-1);
        movieName.setText(m.getTitle());
        movieGenre.setText(genreText);
        movieDuration.setText("Duration: " + m.getRunningTime()+" minutes ");
        movieYear.setText("Released in "+m.getReleaseYear());
        movieBudget.setText("Budget: "+ m.getBudget());
        movieRevenue.setText("Revenue: "+m.getRevenue());
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
    public void buildListView(MovieList movieList){
        myList.getItems().clear();
        List<Button> movieButtons = new ArrayList<>();
        for(Movie m: movieList.getMovieList()){
            Button button = new Button();
            button.setText(m.getTitle());
            button.setStyle(" -fx-background-color: #353535; -fx-text-fill: white; -fx-background-radius: 40;");
            Font font = Font.font("Franklin Gothic Demi", FontWeight.BOLD,25);
            button.setFont(font);
            //button.setMinWidth(30);
            button.setPrefWidth(280);
            button.setPrefHeight(70);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    unHighlightAllMovieButtons(movieButtons);
                    Button b = (Button)e.getSource();
                    b.setStyle(" -fx-background-color: #353535; -fx-text-fill: #00ffff; -fx-background-radius: 40;");
                    String name = b.getText();
                    transferName.setText(name);
                    movieName.setText(name);
                    MovieList mm = movieList.searchByTitle(name);
                    Movie m = mm.getMovieList().get(0);
                    setMovieDetails(m);
                }
            });
            movieButtons.add(button);
        }
        myList.getItems().addAll(movieButtons);
    }

    public void unHighlightAllMovieButtons(List<Button>movieButtons){
        for(Button btn: movieButtons){
            btn.setStyle(" -fx-background-color: #353535; -fx-text-fill: white; -fx-background-radius: 40;");
        }
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
