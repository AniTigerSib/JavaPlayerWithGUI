package org.anitiger.musicplayer.track.containers;

import org.anitiger.musicplayer.track.Track;
import org.anitiger.musicplayer.track.containers.TrackContainer;

import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class Playlist extends TrackContainer {
    @Serial
    private static final long serialVersionUID = 1L; // for serialization
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    private static long globalPlaylistId = 0;
    private long playlistId;
    private String playlistTitle;
    private Date playlistCreatedAt;
    private Date playlistLastUpdatedAt;
    private boolean isSaved;

    public Playlist() {
        playlistId = globalPlaylistId++;
        playlistTitle = "New Playlist";
        try {
            playlistCreatedAt = sdf.parse(sdf.format(new Date()));
            playlistLastUpdatedAt = playlistCreatedAt;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public String Title() {
        return playlistTitle;
    }
    public void addTrack(Track track) throws ParseException {
        tracks.add(track);
        playlistLastUpdatedAt = sdf.parse(sdf.format(new Date()));
    }
    public void shuffle(Track track) {
        Collections.shuffle(tracks);
        tracks.set(0, track);
    }
    public void removeTrack(Track track) throws ParseException {
        tracks.remove(track);
        playlistLastUpdatedAt = sdf.parse(sdf.format(new Date()));
    }
    public Track getFirst() {
        return tracks.getFirst();
    }
    public Track getNext(Track track) {
        int index = tracks.indexOf(track);
        if (index == -1) {
            return null;
        }
        if (index == tracks.size() - 1) {
            return tracks.getFirst();
        }
        return tracks.get(index + 1);
    }
    public Track getPrevious(Track track) {
        int index = tracks.indexOf(track);
        if (index == -1) {
            return null;
        }
        if (index == 0) {
            return tracks.getLast();
        }
        return tracks.get(index - 1);
    }
    @Override
    public void writeExternal(ObjectOutput out) throws java.io.IOException {
        out.writeLong(playlistId);
        out.writeUTF(playlistTitle);
        out.writeUTF(sdf.format(playlistCreatedAt));
        out.writeUTF(sdf.format(playlistLastUpdatedAt));
        out.writeInt(tracks.size());
        for (Track track : tracks) {
            track.writeExternal(out);
        }
        this.isSaved = true;
    }
    @Override
    public void readExternal(ObjectInput in) throws java.io.IOException {
        long idBuffer = in.readLong();
        if (idBuffer <= globalPlaylistId) {
            this.playlistId = globalPlaylistId++;
        } else {
            this.playlistId = idBuffer;
            globalPlaylistId = ++idBuffer;
        }
        playlistTitle = in.readUTF();
        try {
            playlistCreatedAt = sdf.parse(in.readUTF());
            playlistLastUpdatedAt = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            throw new InvalidObjectException("Parse error" + e.getMessage() + e.getStackTrace()[0]);
        }
        int trackCount = in.readInt();
        for (int i = 0; i < trackCount; i++) {
            Track track = new Track();
            track.readExternal(in);
            tracks.add(track);
        }
        this.isSaved = true;
    }
}