package com.example.personaldiary;

import com.example.personaldiary.controllers.DashboardController;
import com.example.personaldiary.controllers.DashboardSettings;
import com.example.personaldiary.controllers.EditorController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Router {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLoginScene(ActionEvent event) throws IOException {
        Parent loginFxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/login.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loginFxml);
        stage.setScene(scene);
    }
    public void switchToRegisterScene(ActionEvent event) throws IOException {
        Parent loginFxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/register.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loginFxml);
        stage.setScene(scene);
    }

    public void switchToDashboardScene(ActionEvent event, User user) {
        // Parent loginFxml = FXMLLoader.load(getClass().getResource("fxml/dashboard.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/dashboard.fxml"));
        Parent dashboardFxml = null;
        try {
            dashboardFxml = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to load dashboard.fxml");
            e.printStackTrace();
        }

        DashboardController dashboardController = loader.getController();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(Objects.requireNonNull(dashboardFxml));
        scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dashboardController.init(user);
            }
        });
        stage.setScene(scene);
    }


    public void switchToDashboardSettings(ActionEvent event, User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/dashboardSettings.fxml"));
        Parent dashboardSettingsFxml = null;
        try {
            dashboardSettingsFxml = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to load dashboardSettings");
            e.printStackTrace();
        }

        DashboardSettings settingsController = loader.getController();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(Objects.requireNonNull(dashboardSettingsFxml));
        scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                settingsController.init(user);
            }
        });
        stage.setScene(scene);
    }

    public void switchToEditor(Event event, User user, Page page) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/editor.fxml"));
        Parent dashboardFxml = null;
        try {
            dashboardFxml = loader.load();
        } catch (IOException e) {
            System.out.println("Failed to load Editor");
            e.printStackTrace();
        }
        EditorController editorController = loader.getController();
        editorController.setPage(page);
        editorController.setUser(user);
        editorController.fetchData(page.getId());

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(Objects.requireNonNull(dashboardFxml));
//        scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                editorController.init(user, page);
//            }
//        });
        stage.setScene(scene);
    }

}
