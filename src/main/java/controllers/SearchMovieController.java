package controllers;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.Movie;
import models.MovieList;
import models.ProductionCompany;
import client.Main;
import util.LogoutDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SearchMovieController {

    private Main main;

    private ConcurrentHashMap<String,String> trailerList;

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

    // v2

    @FXML
    private Button passChangeBtn;
    @FXML
    private ImageView moviePoster;
    @FXML
    private ImageView companyPoster;
    @FXML
    private Label notiLabel;

    @FXML
    private Hyperlink watchLink;
    List<String> dummies = new ArrayList<>();

    @FXML
    private Button searchBtn;
    @FXML
    private Button doSearchBtn;

    @FXML
    private TextField searchGenre;

    @FXML
    private TextField searchTitle;

    @FXML
    private TextField searchYear;


    @FXML
    private Label foundLabel;

    public void clearAllInput(){
        searchGenre.clear();
        searchTitle.clear();
        searchYear.clear();
    }
    @FXML
    void handleTitleKeyPress(KeyEvent event) {

        TextField t = (TextField) event.getSource();
        String s = t.getText();
        if(s.length()<2){
            searchGenre.clear();
            searchYear.clear();
        }
        //System.out.println(s);

    }
    @FXML
    void handleGenreKeyPress(KeyEvent event) {

        TextField t = (TextField) event.getSource();
        String s = t.getText();
        if(s.length()<2){
            searchYear.clear();
            searchTitle.clear();
        }
        //System.out.println(s);

    }
    @FXML
    void handleYearKeyPress(KeyEvent event) {

        TextField t = (TextField) event.getSource();
        String s = t.getText();
        if(s.length()<2){
            searchGenre.clear();
            searchTitle.clear();
        }
        //System.out.println(s);

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
    public void init(ProductionCompany p) {
        main.setCompanyName(p.getName());
        //Image img = new Image(Main.class.getResourceAsStream("img/GoodWillHuntingposter.jpg"));

        MovieList movieList = p.getCompanyMovies();

        unsetMovieDetails();
        hightlight(searchBtn);

        //v2 start

        trailerList = p.getTrailerList();
        //System.out.println(trailerList.size());
        /*for(String s: trailerList.keySet()){
            System.out.println(s + " : " + trailerList.get(s));
        }*/

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

        //v2 end

        //v3

        for(String s: p.getTrailerList().keySet()){
            if(p.getTrailerList().get(s).equals("dummy")){
                dummies.add(s);
            }
        }

        //v3

        buildListView(p.getCompanyMovies());
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
        doSearchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title = searchTitle.getText().strip();
                String genre = searchGenre.getText().strip();
                String year = searchYear.getText().strip();
                if(title.length()>0){
                    MovieList result = movieList.searchByTitle(title);
                    int cnt;
                    if(result==null)
                        cnt = 0;
                    else
                        cnt = result.getMovieCount();
                    foundLabel.setText(cnt + " Movies Found");
                    buildListView(result);
                }
                else if(genre.length()>0){
                    MovieList result = movieList.searchByGenre(genre);
                    int cnt;
                    if(result==null)
                        cnt = 0;
                    else
                        cnt = result.getMovieCount();
                    foundLabel.setText(cnt + " Movies Found");
                    buildListView(result);
                }
                else if(year.length()>0){
                    int y = parseToInt(year);
                    MovieList result = movieList.searchByReleaseYear(y);
                    int cnt;
                    if(result==null)
                        cnt = 0;
                    else
                        cnt = result.getMovieCount();
                    foundLabel.setText(cnt + " Movies Found");
                    buildListView(result);
                }
                else{

                }
            }
        });

    }

    public void unsetMovieDetails(){
        moviePoster.setImage(null);
        movieName.setText("");
        movieGenre.setText("");
        movieDuration.setText("");
        movieYear.setText("");
        movieBudget.setText("");
        movieRevenue.setText("");
    }

    public void setMovieDetails(Movie m){

        //v2

        watchLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                HostServices hostServices = main.getHostServices();
                String link = trailerList.get(m.getTitle());
                String demoLink = "https://www.google.com";
                hostServices.showDocument(link);
                watchLink.setVisited(false);
            }
        });

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

        //v2

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
        unsetMovieDetails();
        if(movieList==null || movieList.getMovieCount()==0)
            return;
        setMovieDetails(movieList.getMovieList().get(0));
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
                    movieName.setText(name);
                    MovieList mm = movieList.searchByTitle(name);
                    Movie m = mm.getMovieList().get(0);
                    setMovieDetails(m);
                }
            });
            movieButtons.add(button);
        }
        myList.getItems().addAll(movieButtons);
        movieButtons.get(0).setStyle(" -fx-background-color: #353535; -fx-text-fill: #00ffff; -fx-background-radius: 40;");  // default highlight first one
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
