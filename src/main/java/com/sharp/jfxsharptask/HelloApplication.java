package com.sharp.jfxsharptask;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(stage);
        path = Path.of(selectedDirectory.getAbsolutePath());
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Host = getHostServices();
        HelloApplication.stage = stage;
        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    static public HostServices Host;

    static public Path path;
    static public Stage stage;
}