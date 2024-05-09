package org.anitiger.musicplayer.track.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.anitiger.musicplayer.track.Track;

public class TrackModel {
    private final ObjectProperty<Long> id = new SimpleObjectProperty<>(0L);
    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty artist = new SimpleStringProperty("");
    private final ObjectProperty<Long> duration = new SimpleObjectProperty<>(0L);

    public TrackModel(Track track) {
        this.id.set(track.getTrackId());
        this.title.set(track.getTrackTitle());
        this.artist.set(track.getTrackAuthors());
        this.duration.set(track.getTrackDuration());
    }
    public TrackModel(long id, String title, String artist, long duration) {
        this.id.set(id);
        this.title.set(title);
        this.artist.set(artist);
        this.duration.set(duration);
    }

    public ObjectProperty<Long> idProperty() {
        return id;
    }
    public StringProperty titleProperty() {
        return title;
    }
    public StringProperty artistProperty() {
        return artist;
    }
    public ObjectProperty<Long> durationProperty() {
        return duration;
    }
}
