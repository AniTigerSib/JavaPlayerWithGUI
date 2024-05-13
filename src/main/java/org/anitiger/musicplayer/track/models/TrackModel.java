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
    private final ObjectProperty<Double> duration = new SimpleObjectProperty<>(0.0);

    public TrackModel(Track track) {
        this.id.set(track.getId());
        this.title.set(track.getTitle());
        this.artist.set(track.getAuthor());
        this.duration.set(track.getDuration());
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
    public ObjectProperty<Double> durationProperty() {
        return duration;
    }
}
