package com.example.personaldiary.controllers;

import com.example.personaldiary.DBConn;
import com.example.personaldiary.Page;
import com.example.personaldiary.Router;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.personaldiary.User;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DashboardController {
    private User user;

    @FXML private Button newBtn;
    @FXML public Label greetingLabel;
    @FXML private TableView<Page> myTable;
    @FXML private TableColumn<Page, String> titleColumn;
    @FXML private TableColumn<Page, Date> dateColumn;


    public void init(User user) {
        System.out.println("Init");
        setUser(user);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory((new PropertyValueFactory<>("datetime")));

        myTable.getColumns().clear();
        myTable.getColumns().add(titleColumn);
        myTable.getColumns().add(dateColumn);
        myTable.setItems(getPages(user));

    }

    public void setUser(User user) {
        this.user = user;
        greetingLabel.setText("Hi, " + user.getName() + "!");
    }

    public ObservableList<Page> getPages(User user) {
        DBConn DB = new DBConn();
        Connection conn = DB.getConnection();
        ObservableList<Page> pages = FXCollections.observableArrayList();

        try {
            Statement getPagesStatement = conn.createStatement();
            String getPagesQuery = String.format("select id, title, datetime from pages where author=%d;", user.getId());
            ResultSet res = getPagesStatement.executeQuery(getPagesQuery);
            while(res.next()){
                int id = res.getInt("id");
                String title = res.getString("title");
                Date datetime = res.getDate("datetime");
                System.out.println(title + " " + datetime);
                pages.add(new Page(id, title, datetime));
            }
        } catch (SQLException e) {
            System.out.println("Failed to getPages");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return pages;
    }

    @FXML
    public void pageItemClicked(MouseEvent event) {
        if(event.getClickCount() == 2){
            int pageId = myTable.getSelectionModel().getSelectedItem().getId();
            System.out.println(myTable.getSelectionModel().getSelectedItem().getId());
            Router router = new Router();
            router.switchToEditor(event, user, new Page(pageId));
        }
    }

    @FXML
    public void newPage(ActionEvent event) throws IOException {
        Router router = new Router();
        Page newPage = new Page(-1);
        router.switchToEditor(event, this.user, newPage);
    }

    @FXML
    public void gotoSettings(ActionEvent event) {
        Router router = new Router();
        router.switchToDashboardSettings(event, this.user);
    }
}
