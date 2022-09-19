package client;

import controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.MovieList;
import models.ProductionCompany;
import util.NetworkUtil;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    String companyName;

    private Stage stage;
    private NetworkUtil networkUtil;

    public Stage getStage() {
        return stage;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        System.out.println("Here");
        connectToServer();
        System.out.println("Connected to Server");
        showLoginPage();
        System.out.println("Can show");
    }

    private void connectToServer() throws IOException {
        String serverAddress = "127.0.0.1";
        int serverPort = 44444;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        new ReadThreadClient(this);
    }

    public void showLoginPage() throws Exception {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login.fxml"));
        Parent root = loader.load();

        // Loading the controller
        LoginController controller = loader.getController();
        controller.setMain(this);
        controller.init();

        // Set the primary stage
        stage.setTitle("Login");
        setIcon(stage);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showHomePage(ProductionCompany p, String option, String noti) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/home.fxml"));
        Parent root = loader.load();

        // Loading the controller
        HomeController controller = loader.getController();
        String listType = "All Movies";
        MovieList movieList = p.getCompanyMovies();
        controller.setMain(this);
        controller.init(p,option,movieList,noti);


        // Set the primary stage
        stage.setTitle("Home");
        setIcon(stage);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showTransferPage(ProductionCompany p) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/transfer.fxml"));
        Parent root = loader.load();

        // Loading the controller
        TransferController controller = loader.getController();
        String listType = "All Movies";
        MovieList movieList = p.getCompanyMovies();
        controller.init(p,listType,movieList);
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Home");
        setIcon(stage);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showAddMoviePage(ProductionCompany p) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/addMovie.fxml"));
        Parent root = loader.load();

        // Loading the controller
        AddMovieController controller = loader.getController();
        String listType = "All Movies";
        MovieList movieList = p.getCompanyMovies();
        controller.init(p);
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Add a Movie");
        setIcon(stage);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void showChangePassPage(ProductionCompany p) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/changePass.fxml"));
        Parent root = loader.load();

        // Loading the controller
        PasswordChangeController controller = loader.getController();
        String listType = "All Movies";
        MovieList movieList = p.getCompanyMovies();
        controller.setMain(this);
        controller.init(p);

        // Set the primary stage
        stage.setTitle("Change Password");
        setIcon(stage);
        stage.setScene(new Scene(root));

        stage.show();
    }
    public void showSearchMoviePage(ProductionCompany p) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/searchMovie.fxml"));
        Parent root = loader.load();

        // Loading the controller
        SearchMovieController controller = loader.getController();
        String listType = "All Movies";
        MovieList movieList = p.getCompanyMovies();
        controller.setMain(this);
        controller.init(p);

        // Set the primary stage
        stage.setTitle("Search Movies");
        setIcon(stage);
        stage.setScene(new Scene(root));

        stage.show();
    }

    public void showAlert(String title, String header, String content) {
        Alert alert;
        if(title.equalsIgnoreCase("Success")){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setIcon(Stage stage){
        //icon

        File file = new File("src/main/resources/images/ic.png");
        Image icon = new Image(file.toURI().toString());

        stage.getIcons().add(icon);

        //icon
    }

    public void destroyClient(){
        System.exit(0);
    }

    public static void main(String[] args) {
        // This will launch the JavaFX application
        launch(args);
    }
}
