package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.ProductionCompany;
import client.Main;
import util.LogoutDTO;
import util.PasswordChangeDTO;

import java.io.IOException;

public class PasswordChangeController {

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
    private Button transferMovieBtn;

    @FXML
    private TextField confirmPassInput;

    @FXML
    private TextField currentPassInput;

    @FXML
    private Button doChangeBtn;
    @FXML
    private Button passChangeBtn;


    @FXML
    private TextField newPassInput;
    @FXML
    private Button searchBtn;


    public void init(ProductionCompany p) {

        message.setText(p.getName());
        hightlight(passChangeBtn);
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
        doChangeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String currentPass = currentPassInput.getText().strip();
                String newPass = newPassInput.getText().strip();
                String confirmPass = confirmPassInput.getText().strip();

                if(currentPass.length()==0 || newPass.length()==0 || confirmPass.length()==0){
                    main.showAlert("Sorry","Sorry","Please Fill All the Fields");
                }

                else if(!(confirmPass.equals(newPass))){
                    main.showAlert("Sorry","Sorry","Passwords don't match");
                }

                else{
                    var pDTO = new PasswordChangeDTO(p.getName(),currentPass,newPass);
                    try {
                        main.getNetworkUtil().write(pDTO);
                        System.out.println("Password Change request sent to server");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }

    public void clearAll(){
        currentPassInput.clear();
        newPassInput.clear();
        confirmPassInput.clear();
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
