package com.example.personaldiary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {
    @FXML private Label title;
    @FXML private Label errorLabel;
    @FXML private TextField regUserId;
    @FXML private PasswordField regUserPass;
    @FXML private TextField regUserPassConfirm;

    @FXML private TextField logUserId;
    @FXML private PasswordField logUserPass;
    private Router router;

    Connection connect(){
        DBConn conn = new DBConn();
        Connection connectDB = conn.getConnection();
        return connectDB;
    }

    @FXML
    protected void toRegister (ActionEvent event) throws IOException {
        router = new Router();
        router.switchToRegisterScene(event);
    }
    @FXML
    protected void toLogin (ActionEvent event) throws IOException {
        router = new Router();
        router.switchToLoginScene(event);
    }

    @FXML
    protected void register (ActionEvent event) {
        errorLabel.setVisible(false);
        router = new Router();
        if(regUserId.getText().isEmpty() || regUserPass.getText().isEmpty() || regUserPassConfirm.getText().isEmpty()){
            errorLabel.setText("Please fill up all the fields");
            errorLabel.setVisible(true);
        }
        else{
            if(!regUserPass.getText().equals(regUserPassConfirm.getText())){
                errorLabel.setText("Password doesn't match!");
                errorLabel.setVisible(true);
            }
            else{
                try {
                    String query = String.format("INSERT INTO `auth`.`users`(`username`, `password`) VALUES ( \"%s\", \"%s\")", regUserId.getText(), regUserPass.getText());;
                    Statement st = connect().createStatement();
                    st.execute(query);
                    try{
                        router.switchToLoginScene(event);
                    } catch(IOException e){
                        System.out.println("Failed to switch to LoginScene");
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    errorLabel.setText((e.getMessage()));
                    errorLabel.setVisible(true);
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    protected void login (ActionEvent event) {
        errorLabel.setVisible(false);
        router = new Router();
        if(logUserId.getText().isEmpty() || logUserPass.getText().isEmpty()){
            errorLabel.setText("Please fill up all the fields");
            errorLabel.setVisible(true);
        }
        else{
            try {
                String query = String.format("select * from users where username=\"%s\";", logUserId.getText());
                Statement st = connect().createStatement();
                ResultSet res = st.executeQuery(query);
                if(!res.next()){
                    errorLabel.setText("User Not Found!");
                    errorLabel.setVisible(true);
                    return;
                }
                do{
                    if(!(res.getString("password").equals(logUserPass.getText()))){
                        errorLabel.setText("Wrong Password");
                        errorLabel.setVisible(true);
                    }else{
                        // Login Sucess
                        String username = res.getString("username");
                        int id = res.getInt("idusers");
                        User user = new User(id, username);
                        router.switchToDashboardScene(event, user);
                    }
                }
                while(res.next());


            } catch (SQLException e) {
                // the reason for the exception
                e.printStackTrace();

            }
        }
    }

}