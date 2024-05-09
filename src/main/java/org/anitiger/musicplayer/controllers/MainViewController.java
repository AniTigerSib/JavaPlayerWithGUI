package org.anitiger.musicplayer.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.anitiger.musicplayer.App;
import org.anitiger.musicplayer.track.Track;
import org.anitiger.musicplayer.track.containers.Playlist;
import org.anitiger.musicplayer.track.models.TrackModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

public class MainViewController {
    @FXML
    protected AnchorPane anchorPane;
    @FXML
    protected ImageView backButton;
    @FXML
    protected ImageView playButton;
    @FXML
    protected ImageView nextButton;
    // protected ImageView shuffleButton;
    @FXML
    protected ChoiceBox<String> playlistChoiceBox;
    @FXML
    protected TableView<TrackModel> playlistTableView;
    @FXML
    protected TableColumn<TrackModel, Long> trackIdColumn;
    @FXML
    protected TableColumn<TrackModel, String> trackTitleColumn;
    @FXML
    protected TableColumn<TrackModel, String> trackArtistColumn;
    @FXML
    protected TableColumn<TrackModel, Long> trackDurationColumn;
    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);
    @FXML
    protected void onPlayButtonClicked() {
        if (App.isNowPlaying) {
            App.isNowPlaying = false;
            // App.StopTrack();
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/pause.png"))));
        } else {
            App.isNowPlaying = true;
            // App.PlayTrack(null);
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/play.png"))));
        }
    }
    @FXML
    protected void onNextButtonClicked() {
        App.PlayNext();
    }
    @FXML
    protected void onBackButtonClicked() {
        App.PlayPrevious();
    }
    @FXML
    protected void onActionAddPlaylistButton() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("PlaylistAddWindow.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Playlist");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(anchorPane.getScene().getWindow());
            stage.showAndWait();
            updatePlaylists();
        } catch (IOException e) {
            logger.error("Failed to load PlaylistAddWindow.fxml; Cause: " + e.getMessage());
        }
    }

    @FXML
    protected void onActionSaveButton() {
        String dirPath = "/home/" + System.getProperty("user.name") + "/Music/";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            logger.info("Creating directory " + dirPath);
            boolean res = false;
            try {
                res = dir.mkdir();
            } catch (SecurityException e) {
                logger.error("Failed to create directory " + dirPath + "; Cause: " + e.getMessage());
            }
            if (!res) {
                logger.error("Failed to create directory " + dirPath);
            } else {
                logger.info("Directory " + dirPath + " created");
            }
        }
        File file = new File(dirPath + App.currentPlaylist.getTitle());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            App.currentPlaylist.writeExternal(oos);
            oos.close();
            logger.info("Playlist " + App.currentPlaylist.getTitle() + " saved");
        } catch (IOException e) {
            logger.error("Failed to save playlist " + App.currentPlaylist.getTitle() + "; Cause: " + e.getMessage());
        }
    }

    protected void updatePlaylists() {
        ObservableList<String> playlists = FXCollections.observableArrayList();
        for (int i = 0; i < App.playlists.size(); i++) {
            playlists.add(App.playlists.get(i).getTitle());
        }
        playlistChoiceBox.setItems(playlists);
    }

    protected void updateTrackTableView() {
        ObservableList<TrackModel> tracks = FXCollections.observableArrayList();
        for (int i = 0; i < App.currentPlaylist.getTracks().size(); i++) {
            tracks.add(new TrackModel(App.currentPlaylist.getTracks().get(i)));
        }
        playlistTableView.setItems(tracks);
    }

    @FXML
    public void initialize() {
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/back.png")).toString()));
        playButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/play.png")).toString()));
        nextButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/next.png")).toString()));
        updatePlaylists();
        // if the item of the list is changed
        playlistChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            // set the new value
            App.currentPlaylist = App.playlists.get(new_value.intValue());
            updateTrackTableView();
            // set the text for the label to the selected item
            logger.info("Playlist " + App.currentPlaylist.getTitle() + " selected");
        });
        TableView.TableViewSelectionModel<TrackModel> selectionModel = playlistTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener((ov, value, new_value) -> {
            if (new_value != null) {
                App.currentTrack = App.currentPlaylist.getTrackById(new_value.idProperty().get());
                logger.info("Track " + App.currentTrack.getTrackTitle() + " selected");
            }
        });
        trackIdColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, Long>("id"));
        trackTitleColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, String>("title"));
        trackArtistColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, String>("artist"));
        trackDurationColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, Long>("duration"));
        logger.info("MainViewController initialized");
    }
}
