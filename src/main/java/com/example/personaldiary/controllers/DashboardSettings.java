package com.example.personaldiary.controllers;

import com.example.personaldiary.DBConn;
import com.example.personaldiary.Router;
import com.example.personaldiary.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DashboardSettings {
    private User user;

    @FXML private TextField nameField;
    @FXML private TextField currentPassword;
    @FXML private TextField newPassword;
    @FXML private TextField confirmPassword;

    @FXML
    public void init(User user) {
        System.out.println("Init Settings");
        setUser(user);
        System.out.println(this.nameField);

        fetchData();
    }

    private void fetchData(){
        DBConn DB = new DBConn();
        Connection conn = DB.getConnection();
        try {
            Statement st = conn.createStatement();
            String query = String.format("select username from users where idusers=%d;", user.getId());
            ResultSet res = st.executeQuery(query);
            while(res.next()){
                String username = res.getString("username");
                user.setName(username);
                nameField.setText(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setUser(User user) {
        this.user = user;
        System.out.println(this.user.getName());
    }

    @FXML
    public void gotoHome(ActionEvent event) {
        Router router = new Router();
        router.switchToDashboardScene(event, user);
    }

    @FXML
    public void changeName() {
        if(!nameField.getText().isEmpty()){
            System.out.println("Changing Name");
            DBConn DB = new DBConn();
            Connection conn = DB.getConnection();
            try {
                Statement st = conn.createStatement();
                String query = String.format("UPDATE `auth`.`users` SET `username` = \"%s\" WHERE `idusers` = \"%s\";", nameField.getText(), user.getId());
                st.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println("Failed to update username");
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void changePassword() {
        if(!currentPassword.getText().isEmpty() || !newPassword.getText().isEmpty() || !confirmPassword.getText().isEmpty()) {
            if(newPassword.getText().equals(confirmPassword.getText())){
                DBConn DB = new DBConn();
                Connection conn = DB.getConnection();
                try {
                    Statement st = conn.createStatement();
                    String query = String.format("UPDATE `auth`.`users` SET `password` = \"%s\" WHERE `idusers` = \"%s\";", newPassword.getText(), user.getId());
                    st.executeUpdate(query);
                } catch (SQLException e) {
                    System.out.println("Failed to change password");
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Password doesn't match");
            }
        }

    }
}
