package org.anitiger.musicplayer.track.containers;

import org.anitiger.musicplayer.track.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final Logger logger = LoggerFactory.getLogger(Playlist.class);
    public Playlist() {
        playlistId = globalPlaylistId++;
        playlistTitle = "New Playlist";
        tracks = new ArrayList<>();
        try {
            playlistCreatedAt = sdf.parse(sdf.format(new Date()));
            playlistLastUpdatedAt = playlistCreatedAt;
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
    }
    public Playlist(String title) {
        this();
        playlistTitle = title;
    }
    public Playlist(File file) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        this.readExternal(ois);
        ois.close();
    }
    public String getTitle() {
        return playlistTitle;
    }
    public ArrayList<Track> getTracks() {
        return tracks;
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(playlistId);
        out.writeUTF(playlistTitle);
        out.writeUTF(sdf.format(playlistCreatedAt));
        for (Track track : tracks) {
            track.writeExternal(out);
        }
        this.isSaved = true;
    }
    @Override
    public void readExternal(ObjectInput in) throws IOException {
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
            throw new InvalidObjectException("Parse error" + e.getMessage());
        }
        while (in.available() > 0){
            Track track = new Track();
            track.readExternal(in);
            tracks.add(track);
        }
        this.isSaved = true;
    }
}