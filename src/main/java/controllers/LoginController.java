package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import client.Main;
import util.LoginDTO;

import java.io.File;
import java.io.IOException;


public class LoginController {

    private Main main;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button resetButton;

    @FXML
    private Button loginButton;
    @FXML
    private ImageView iconImage;
    @FXML
    private ImageView manImg;

    @FXML
    private ImageView passImg;

    public void init(){

        Image img = loadImageFromFile("src/main/resources/images/ic.png");
        iconImage.setImage(img);

        img = loadImageFromFile("src/main/resources/images/man.png");
        manImg.setImage(img);

        img = loadImageFromFile("src/main/resources/images/pass.png");
        passImg.setImage(img);


    }

    public Image loadImageFromFile(String path){

        File file = new File(path);
        Image img = new Image(file.toURI().toString());

        return img;
    }
    @FXML
    public void loginAction(ActionEvent event) {
        String userName = userText.getText();
        String password = passwordText.getText();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUserName(userName);
        loginDTO.setPassword(password);
        try {
            main.getNetworkUtil().write(loginDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void resetAction(ActionEvent event) {
        userText.setText(null);
        passwordText.setText(null);
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
