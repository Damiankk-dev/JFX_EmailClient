module FirstJavaFx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;

    opens emailClient.main;
    opens emailClient.main.view;
    opens emailClient.main.controller;
}