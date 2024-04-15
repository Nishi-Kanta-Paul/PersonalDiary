package com.example.personaldiary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        stage.setTitle("Personal Diary");
        stage.setScene(scene);
        Image icon=new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/logo.png")));
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}

