package org.anitiger.musicplayer.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.anitiger.musicplayer.App;
import org.anitiger.musicplayer.track.Track;
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
    protected Label nextTrackLabel;
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
    protected TableColumn<TrackModel, Double> trackDurationColumn;
    TableView.TableViewSelectionModel<TrackModel> selectionModel;
    private static final Logger logger = LoggerFactory.getLogger(MainViewController.class);
    private Track trackToPlay;
    @FXML
    protected void onPlayButtonClicked() {
        if (App.mediaPlayer != null && App.mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            App.StopTrack();
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/play.png"))));
        } else {
            App.PlayTrack(trackToPlay);
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/pause.png"))));
        }
        updateNextTrackLabel();
    }
    @FXML
    protected void onNextButtonClicked() {
        App.PlayNext();
        updateNextTrackLabel();
    }
    @FXML
    protected void onBackButtonClicked() {
        App.PlayPrevious();
        updateNextTrackLabel();
    }
    @FXML
    protected void onActionAddPlaylistButton() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("playlist-add-view.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Playlist");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(anchorPane.getScene().getWindow());
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
            updatePlaylists();
            updateTrackTableView();
        } catch (IOException e) {
            logger.error("Failed to load playlist-add-view.fxml; Cause: " + e.getMessage());
        }
    }
    @FXML
    protected void onActionRemovePlaylistButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Playlist");
        alert.setHeaderText("Are you sure you want to remove this playlist?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            App.RemovePlaylist();
            updatePlaylists();
            updateTrackTableView();
        } else {
            logger.info("User cancelled removing playlist.");
        }
    }
    @FXML
    protected void onActionAddTrackButton() {
        if (App.currentPlaylist == null) {
            logger.warn("No playlist selected");
            return;
        }
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("track-add-view.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Track");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(anchorPane.getScene().getWindow());
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
            updateTrackTableView();
        } catch (IOException e) {
            logger.error("Failed to load track-add-view.fxml; Cause: " + e.getMessage());
        }
    }
    @FXML
    protected void onActionRemoveTrackButton() {
        if (trackToPlay == null) {
            logger.warn("No track selected");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Track");
        alert.setHeaderText("Are you sure you want to remove this track?");
        alert.setContentText("This action cannot be undone.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            App.RemoveTrack(trackToPlay);
            updateTrackTableView();
        } else {
            logger.info("User cancelled removing track.");
        }
    }
    @FXML
    protected void onActionSaveButton() {
        String dirPath = "C:\\users" + System.getProperty("user.name") + "\\Music\\";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            logger.debug("Creating directory " + dirPath);
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
        if (App.currentPlaylist == null) {
            playlistTableView.setItems(tracks); // empty table
            return;
        }
        for (int i = 0; i < App.currentPlaylist.getTracks().size(); i++) {
            tracks.add(new TrackModel(App.currentPlaylist.getTracks().get(i)));
        }
        playlistTableView.setItems(tracks);
    }

    protected void updateNextTrackLabel() {
        if (App.currentTrack != null) {
            nextTrackLabel.setText(App.currentPlaylist.getNext(App.currentTrack).getTitle());
        }
    }

    @FXML
    public void initialize() {
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/back.png")).toString()));
        playButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/play.png")).toString()));
        nextButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/next.png")).toString()));
        updatePlaylists();
        // if the item of the list is changed
        playlistChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            if (new_value.intValue() >= 0) {
                App.currentPlaylist = App.playlists.get(new_value.intValue());
                logger.debug("Playlist " + App.currentPlaylist.getTitle() + " selected");
            } else {
                App.currentPlaylist = null;
                logger.debug("No playlist selected");
            }
            App.StopTrack();
            updateTrackTableView();
        });
        selectionModel = playlistTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener((ov, value, new_value) -> {
            if (new_value != null) {
                trackToPlay = App.currentPlaylist.getTrackById(new_value.idProperty().get());
                logger.debug("Track " + trackToPlay.getTitle() + " selected");
            }
        });
        trackIdColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, Long>("id"));
        trackTitleColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, String>("title"));
        trackArtistColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, String>("artist"));
        trackDurationColumn.setCellValueFactory(new PropertyValueFactory<TrackModel, Double>("duration"));
        logger.info("MainViewController initialized");
    }
}
