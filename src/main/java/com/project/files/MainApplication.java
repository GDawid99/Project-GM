package com.project.files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {

    public static final int HEIGHT_WINDOW = 805;
    public static final int WIDTH_WINDOW = 713;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HEIGHT_WINDOW, WIDTH_WINDOW);
        String css = Objects.requireNonNull(this.getClass().getResource("view.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setResizable(false);
        stage.setTitle("Program generowania mikrostruktury");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}