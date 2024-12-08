module FirstJavaFx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;

    opens emailClient.main;
    opens emailClient.main.view;
    opens emailClient.main.controller;
    opens emailClient.main.model;
}