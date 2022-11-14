package com.sharp.jfxsharptask;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<File> filesList;

    @FXML
    private Button updateButton;

    @FXML
    private Label updateLabel;

    private long directoryLastModified;

    private DirectoryWatcher watcher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        watcher = new DirectoryWatcher(HelloApplication.path);
        directoryLastModified = watcher.lastModified();
        updateLabel.setText("Messages:");

        filesList.setOnMouseClicked(e -> {
            if (!filesList.getItems().isEmpty()) {
                HelloApplication.Host.showDocument(filesList.getSelectionModel().getSelectedItem().getPath());
            }
        });
        searchButton.setOnMouseClicked(e -> {
            filesList.getItems().clear();
            updateLabel.setText("");
            var words = List.of(searchField.getText().split(", *"));
            watcher.changeWords(words);
            filesList.getItems().addAll(watcher.getFilesWithUsages());
        });

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (directoryLastModified != watcher.lastModified()) {
                        directoryLastModified = watcher.lastModified();
                        Platform.runLater(() -> updateLabel.setText("Directory was changed. Search again"));
                    }
                }
            }
        };


        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

}
