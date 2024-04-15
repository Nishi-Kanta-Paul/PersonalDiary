package com.example.personaldiary;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
    public Connection DBLink;

    public Connection getConnection() {
        String DBName = "auth";
        String DBUser = "root";
        String DBPass = "KingKon963";

        String url = "jdbc:mysql://localhost:3306/" + DBName;
        // jdbc:mysql://127.0.0.1:3306/?user=root

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBLink = DriverManager.getConnection(url, DBUser, DBPass);
        }catch (Exception e){
            e.printStackTrace();
        }
        return DBLink;
    }

}
