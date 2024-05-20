package org.anitiger.musicplayer;

import org.anitiger.musicplayer.track.Track;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import org.anitiger.musicplayer.track.containers.Playlist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class App extends Application {
    public static ArrayList<Playlist> playlists = new ArrayList<>();
    public static Track currentTrack;
    public static Playlist currentPlaylist;
    public static Media media;
    public static MediaPlayer mediaPlayer;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void RemovePlaylist() {
        StopTrack();
        playlists.remove(currentPlaylist);
        currentPlaylist = null;
        currentTrack = null;
        media = null;
        mediaPlayer = null;
    }

    public static void RemoveTrack(Track track) {
        if (track == currentTrack) {
            StopTrack();
            Track trackToPlay = currentPlaylist.getNext(track);
            if (trackToPlay != currentTrack) {
                try {
                    currentPlaylist.removeTrack(currentTrack);
                } catch (ParseException e) {
                    logger.error("Error parsing time: " + currentTrack.getTitle() + ":\n", e);
                }
                PlayTrack(trackToPlay);
            }
        } else {
            try {
                currentPlaylist.removeTrack(track);
            } catch (ParseException e) {
                logger.error("Error parsing time: " + track.getTitle() + ":\n", e);
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 650);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // Track playing //////////////////////////////////////////////////////////////
    public static void PlayTrack(Track track) {
        if (track != null) {
            currentTrack = track;
            media = null;
            mediaPlayer = null;
        } else if (currentTrack == null) {
            logger.warn("No track to play");
            return;
        }
        try {
            if (mediaPlayer == null) {
                String path = currentTrack.getTrackPath().getAbsolutePath().replace("\\", "/");
                logger.debug("file:/" + path);
                media = new Media("file:/" + path);
                mediaPlayer = new MediaPlayer(media);
            }
            mediaPlayer.play();
            logger.info("Playing track: " + currentTrack.getTitle());
        } catch (Exception e) {
            logger.error("Error playing track: " + currentTrack.getTitle() + ":\n" + e);
        }
        currentTrack = track;
    }
    public static void StopTrack() {
        if (mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.stop();
            logger.info("Stopped track: " + currentTrack.getTitle());
        } else {
            logger.warn("No track to stop");
        }
    }
    public static void PlayNext() {
        if (currentTrack == null) {
            return;
        }
        Track nextTrack = currentPlaylist.getNext(currentTrack);
        PlayTrack(nextTrack);
    }
    public static void PlayPrevious() {
        if (currentTrack == null) {
            return;
        }
        Track previousTrack = currentPlaylist.getPrevious(currentTrack);
        PlayTrack(previousTrack);
    }

    // Playlist interaction //////////////////////////////////////////////////////////
    public static boolean isPlaylistAlreadyExists(Playlist playlist) {
        for (Playlist p : playlists) {
            if (p.getTitle().equals(playlist.getTitle())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        logger.info("Application started");
        launch();
    }
}