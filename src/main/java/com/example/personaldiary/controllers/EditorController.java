package com.example.personaldiary.controllers;

import com.example.personaldiary.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

public class EditorController {
    private User user;
    private Page page;
    @FXML private TextField title;
    @FXML private HTMLEditor textArea;

    @FXML
    public void init(User user, Page page) {
        setUser(user);
        this.page = page;
        System.out.println(page.getId());
        fetchData(page.getId());
    }

    public void fetchData(int pageId){
        DBConn DB = new DBConn();
        Connection conn = DB.getConnection();

        try {
            Statement st = conn.createStatement();
            String query = String.format("select title, datetime, content, author from pages where id=%d;", pageId);
            ResultSet res = st.executeQuery(query);
            while(res.next()){
                String title = res.getString("title");
                String content = res.getString("content");
                Date datetime = res.getDate("datetime");
                int author = res.getInt("author");
                page.setTitle(title);
                page.setContent((content));
                page.setDatetime(datetime);
                page.setAuthor(author);
                this.title.setText(title);
                this.textArea.setHtmlText(content);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch Page data");
            e.printStackTrace();
        }
    }

    private String encryptContent(String input) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException  {
        SecretKey key = Encryption.generateKey(128);
        IvParameterSpec ivParameterSpec = Encryption.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";
        String cipherText = Encryption.encrypt(algorithm, input, key, ivParameterSpec);
        // String plainText = Encryption.decrypt(algorithm, cipherText, key, ivParameterSpec);
        return cipherText;
    }

    @FXML
    public void save(){
        String titleText = title.getText();
        String content = textArea.getHtmlText();
        int authorId = user.getId();
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());

        DBConn DB = new DBConn();
        Connection conn = DB.getConnection();

        page.setTitle(titleText);
        page.setContent(content);
        page.setAuthor(authorId);
        page.setDatetime(time);

        // Encrypting the content
        try {
            String cipherContent =  encryptContent(content);
            System.out.println(cipherContent);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        if(page.getId() == -1){ // Create new Page entry
            try{
                String query = String.format("INSERT INTO `auth`.`pages` (`title`, `content`,`author`,`datetime`) VALUES (\"%s\" , \"%s\", %d, \"%s\");", page.getTitle(), page.getContent(), page.getAuthor(), page.getDatetime());
                Statement st = conn.createStatement();
                st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

                ResultSet res = st.getGeneratedKeys();

                while(res.next()){
                    int generatedKey = res.getInt("GENERATED_KEY");
                    System.out.println(generatedKey);
                    page.setId(generatedKey);
                }
            } catch (SQLException e){
                System.out.println("Error saving the Page");
                e.printStackTrace();
            }
        }
        else{
//            Page will update
            System.out.println("Page Update");
            String query = String.format("UPDATE `auth`.`pages` SET `title` = \"%s\", `content` = \"%s\" WHERE `id` = %d;", page.getTitle(), page.getContent(), page.getId());
            try {
                Statement updateStatement = conn.createStatement();
                int affectedCols = updateStatement.executeUpdate(query);
                System.out.println(affectedCols);
            } catch (SQLException e) {
                System.out.println("Failed to update Page");
                e.printStackTrace();
            }
        }


    }

    @FXML
    public void delete(ActionEvent event){
        // Check if the page is available to delete
        if(page.getId() == -1) return;
        // delete from DB
        DBConn DB = new DBConn();
        Connection conn = DB.getConnection();
        String delQuery = String.format("delete from pages where id=%d;", page.getId());
        try {
            Statement deleteStatement = conn.createStatement();
            int colAffected = deleteStatement.executeUpdate(delQuery);
            System.out.println(colAffected);
        } catch (SQLException e) {
            System.out.println("Failed to delete Page");
            e.printStackTrace();
        }
        // set Page id to -1
        page.setId(-1);
        // Goto Dashboard
        Router router = new Router();
        router.switchToDashboardScene(event, user);
    }

    @FXML
    public void back(ActionEvent event){
        Router router = new Router();
        router.switchToDashboardScene(event, user);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
