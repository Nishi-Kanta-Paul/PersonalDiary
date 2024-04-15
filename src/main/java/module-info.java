module com.example.personaldiary {
    // https://github.com/Kingkon963/PersonalDiary

    requires javafx.controls;
    requires javafx.fxml;
    // requires javafx.web;
    requires  java.sql;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    // requires mysql.connector.java;

    opens com.example.personaldiary to javafx.fxml;
    opens com.example.personaldiary.controllers to javafx.fxml;
    exports com.example.personaldiary;
    exports com.example.personaldiary.controllers;
}