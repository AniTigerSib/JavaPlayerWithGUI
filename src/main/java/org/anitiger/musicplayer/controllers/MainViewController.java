package org.anitiger.musicplayer.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.anitiger.musicplayer.App;
import org.anitiger.musicplayer.track.containers.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    protected Label playlistName;
    @FXML
    protected ImageView backButton;
    @FXML
    protected ImageView playButton;
    @FXML
    protected ImageView nextButton;
    // protected ImageView shuffleButton;
    @FXML
    protected ChoiceBox<String> playlistChoiceBox;
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
//    @FXML
//    protected void onPlaylistCbClicked() {
//        if (playlistChoiceBox.showingProperty().get()) {
//            playlistChoiceBox.hide();
//            try {
//                App.currentPlaylist = App.playlists.get(playlistChoiceBox.getSelectionModel().getSelectedIndex());
//            } catch (IndexOutOfBoundsException e) {
//                logger.error("Playlist not found");
//            }
//        } else {
//            playlistChoiceBox.show();
//        }
////        App.currentPlaylist = App.playlists.get(playlistChoiceBox.getSelectionModel().getSelectedIndex());
////        playlistName.setText(App.currentPlaylist.Title());
//    }

    public void setPlaylistName(String playlistName) {
        this.playlistName.setText(playlistName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/back.png")).toString()));
        playButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/play.png")).toString()));
        nextButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/img/next.png")).toString()));
        ObservableList<String> playlistTitles = FXCollections.observableArrayList();
        App.playlists.forEach(playlist -> {
            playlistTitles.add(playlist.Title());
        });
        playlistChoiceBox.setItems(FXCollections.observableArrayList(playlistTitles));
        // if the item of the list is changed
        playlistChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ov, value, new_value) -> {
            // set the new value
            App.currentPlaylist = App.playlists.get(new_value.intValue());
            playlistName.setText(App.currentPlaylist.Title());
            // set the text for the label to the selected item
            logger.info("Playlist " + playlistName + " selected");
        });
//        playlistChoiceBox.setItems(playlistTitles);
    }
}
