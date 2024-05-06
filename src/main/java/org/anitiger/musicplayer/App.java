package org.anitiger.musicplayer;

import org.anitiger.musicplayer.track.Track;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

import org.anitiger.musicplayer.track.containers.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class App extends Application {
    public static boolean isNowPlaying = false;
    public static ArrayList<Playlist> playlists = new ArrayList<>();
    public static Track currentTrack;
    public static Playlist currentPlaylist;
    public static Media media;
    public static MediaPlayer mediaPlayer;
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 550);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static String getMeta() {
        String thePath = "/home/michael/Downloads/SOLO LEVELING Original Soundtrack  Hiroyuki SAWANO MP3/01 - DARK ARIA.mp3";
        File myFile = new File(thePath);
        Media music = new Media(myFile.toURI().toString());
        var ref = new Object() {
            String toReturn = "42";
        };
        MediaPlayer mediaPlayer = new MediaPlayer(music);
        return ref.toReturn;
    }

    public static void PlayTrack(Track track) {
        if (track == null) {
            mediaPlayer.play();
        } else {
            currentTrack = track;
            media = new Media(currentTrack.getTrackPath().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        logger.info("Playing track: " + currentTrack.getTrackTitle());
        currentTrack = track;
        isNowPlaying = true;
    }
    public static void StopTrack() {
        mediaPlayer.stop();
        isNowPlaying = false;
    }
    public static void PlayNext() {
        if (currentTrack == null) {
            return;
        }
        Track nextTrack = currentPlaylist.getNext(currentTrack);
        PlayTrack(nextTrack);
    }
    public void PlayPlaylist(Playlist playlist) {
        currentPlaylist = playlist;
        PlayTrack(currentPlaylist.getFirst());
    }

    public static void main(String[] args) {
        logger.info("Application started");
        launch();
    }
}