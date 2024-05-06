package org.anitiger.musicplayer.track;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Track implements Externalizable {
    private static final SimpleDateFormat sdfForReleaseDate = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfForAddedAt = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
    private static long globalTrackId = 0;
    private long trackId;
    private String trackTitle;
    private String trackAuthors;
    private long trackDuration; // in seconds
    private String genre;
    private Date trackAddedAt;
    private File trackPath;

    public Track() {
        trackId = globalTrackId++;
        trackTitle = "";
        trackAuthors = "";
        trackDuration = 0;
        genre = "";
        trackPath = null;
        try {
            trackAddedAt = sdfForAddedAt.parse(sdfForAddedAt.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Track(String trackTitle, String trackAuthors, long trackDuration, String genre, File trackPath) throws ParseException {
        if (trackTitle.isEmpty() || trackAuthors.isEmpty() || trackDuration == 0 || genre.isEmpty()) {
            throw new IllegalArgumentException("Track title, track authors, track duration and genre must not be empty.");
        }
        this.trackId = globalTrackId++;
        this.trackTitle = trackTitle;
        this.trackAuthors = trackAuthors;
        this.trackDuration = trackDuration;
        this.genre = genre;
        this.trackPath = trackPath;
        this.trackAddedAt = sdfForAddedAt.parse(sdfForAddedAt.format(new Date()));
    }

    public long getTrackId() {
        return this.trackId;
    }

    public String getTrackTitle() {
        return this.trackTitle;
    }

    public String getTrackAuthors() {
        return this.trackAuthors;
    }

    public long getTrackDuration() {
        return this.trackDuration;
    }

    public String getGenre() {
        return this.genre;
    }
    public File getTrackPath() {
        return this.trackPath;
    }

    public Date getTrackAddedAt() {
        return this.trackAddedAt;
    }
    public void print() {
        System.out.println("Title: " + this.trackTitle);
        System.out.println("\tAuthors: " + this.trackAuthors);
        System.out.println("\tID: " + this.trackId);
        System.out.println("\tDuration: " + this.trackDuration);
        System.out.println("\tGenre: " + this.genre);
        System.out.println("\tTrack path: " + this.trackPath.getAbsolutePath());
        System.out.println("\tTrack was added: " + sdfForAddedAt.format(this.trackAddedAt));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(trackId);
        out.writeUTF(trackTitle);
        out.writeUTF(trackAuthors);
        out.writeLong(trackDuration);
        out.writeUTF(genre);
        out.writeUTF(trackPath.getAbsolutePath());
        out.writeUTF(sdfForAddedAt.format(trackAddedAt));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        long idBuffer = in.readLong();
        if (idBuffer <= globalTrackId) {
            this.trackId = globalTrackId++;
        } else {
            this.trackId = idBuffer;
            globalTrackId = ++idBuffer;
        }
        this.trackTitle = in.readUTF();
        this.trackAuthors = in.readUTF();
        long durationBuffer = in.readLong();
        if (durationBuffer <= 0) {
            throw new InvalidObjectException("Track duration is negative or zero");
        }
        this.trackDuration = durationBuffer;
        this.genre = in.readUTF();
        try {
            this.trackPath = new File(in.readUTF());
        } catch (IOException e) {
            throw new InvalidObjectException("Track path is invalid");
        }
        try {
            this.trackAddedAt = sdfForAddedAt.parse(in.readUTF());
        } catch (ParseException e) {
            throw new InvalidObjectException("Parse error" + e.getMessage() + e.getStackTrace()[0]);
        }
    }
}
