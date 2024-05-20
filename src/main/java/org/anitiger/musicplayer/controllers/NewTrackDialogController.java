package org.anitiger.musicplayer.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.anitiger.musicplayer.App;
import org.anitiger.musicplayer.track.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class NewTrackDialogController {
    @FXML
    AnchorPane rootPane;
    @FXML
    TextField titleField;
    @FXML
    TextField authorField;
    @FXML
    TextField genreField;
    @FXML
    Button fileChoseButton;
    @FXML
    Button formCancelButton;
    @FXML
    Button formAcceptButton;
    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    private static final Logger logger = LoggerFactory.getLogger(NewTrackDialogController.class);

    @FXML
    protected void onFileChose() {
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            logger.debug("Selected file: " + selectedFile.getAbsolutePath());
        }
    }
    @FXML
    protected void onAccept() {
        if (titleField.getText().isEmpty()) {
            logger.warn("Title field is empty");
            return;
        }
        if (authorField.getText().isEmpty()) {
            logger.warn("Author field is empty");
            return;
        }
        if (genreField.getText().isEmpty()) {
            logger.warn("Genre field is empty");
            return;
        }
        if (selectedFile == null) {
            logger.warn("No file selected");
            return;
        }
        try {
            String path = selectedFile.toURI().toString().replace("\\", "/");
            Media media = new Media(path);
            Track track = new Track(titleField.getText(), authorField.getText(), media.getDuration().toSeconds(), genreField.getText(), selectedFile);
            App.currentPlaylist.addTrack(track);
            logger.info("Added track: " + track.getTitle());
            ((Stage)rootPane.getScene().getWindow()).close();
        } catch (Exception e) {
            logger.error("Error while creating new track", e);
        }
    }
    @FXML
    protected void onCancel() {
        ((Stage)rootPane.getScene().getWindow()).close();
    }
    @FXML
    private void initialize() {
        logger.info("NewTrackDialogController initialized");
    }
}