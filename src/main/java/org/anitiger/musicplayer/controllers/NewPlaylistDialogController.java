package org.anitiger.musicplayer.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.anitiger.musicplayer.App;
import org.anitiger.musicplayer.track.containers.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class NewPlaylistDialogController {
    @FXML
    AnchorPane rootPane;
    @FXML
    Button fileChoseButton;
    @FXML
    Button formCancelButton;
    @FXML
    Button formAcceptButton;
    @FXML
    TextField titleField;

    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    private static final Logger logger = LoggerFactory.getLogger(NewPlaylistDialogController.class);

    @FXML
    protected void onFileChose() {
        selectedFile = fileChooser.showOpenDialog(null);
        logger.info("Selected file: " + selectedFile.getAbsolutePath());
    }

    @FXML
    protected void onAccept() {
        try {
            Playlist playlistToAdd = null;
            if (selectedFile == null) {
                if (titleField.getText().isEmpty()) {
                    logger.warn("No file selected and no title entered");
                    return;
                }
                playlistToAdd = new Playlist(titleField.getText());
            } else {
                playlistToAdd = new Playlist(selectedFile);
                logger.info("Playlist uploaded from file: " + selectedFile.getAbsolutePath());
            }
            if (App.isPlaylistAlreadyExists(playlistToAdd)) {
                logger.warn("Playlist already exists");
                return;
            }
            App.playlists.add(playlistToAdd);
            logger.info("Added playlist: " + playlistToAdd.getTitle());
            ((Stage)rootPane.getScene().getWindow()).close();
        } catch (Exception e) {
            logger.error("Error while adding playlist: " + e.getMessage() + "\n" + e.getStackTrace()[0]);
        }

    }
    @FXML
    protected void onCancel() {
        ((Stage)rootPane.getScene().getWindow()).close();
    }
	@FXML
	private void initialize() {
        titleField.setText("");
        logger.info("NewPlaylistDialogController initialized");
    }
}
