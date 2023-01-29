module it.ispw.booknook {
    requires java.sql;
    requires bcrypt;
    requires java.json;
    requires com.esri.arcgisruntime;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires commons.validator;
    requires java.desktop;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires org.apache.commons.codec;
    requires mail;
    requires jdk.unsupported;
    requires jdk.httpserver;
    requires org.controlsfx.controls;


    opens it.ispw.booknook to javafx.fxml;
    exports it.ispw.booknook;
    exports it.ispw.booknook.logic.boundary.main_view;
    opens it.ispw.booknook.logic.boundary.main_view to javafx.fxml;
    exports it.ispw.booknook.logic.entity;
    opens it.ispw.booknook.logic.entity to javafx.fxml;
    exports it.ispw.booknook.logic.bean;
    opens it.ispw.booknook.logic.bean to javafx.fxml;
    exports it.ispw.booknook.logic.boundary;
    opens it.ispw.booknook.logic.boundary to javafx.fxml;
    exports it.ispw.booknook.logic.control;
    exports it.ispw.booknook.logic.exception;
    exports it.ispw.booknook.logic.boundary.secondary_view;

}