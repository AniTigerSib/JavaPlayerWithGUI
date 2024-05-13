package org.anitiger.musicplayer;

import org.anitiger.musicplayer.track.Track;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;

import static junit.framework.Assert.assertEquals;

public class TrackTest {
    @Test
    public void testGetTitle_emptyTitle() {
        Track track = new Track();
        assertEquals("", track.getTitle());
    }

    @Test
    public void testGetTitle_withTitle() throws ParseException {
        Track track = new Track("Some Title", "Artist Name", 123.45, "Rock", new File("/home/michael/Downloads/JairaBurns-Goddess.mp3"));
        assertEquals("Some Title", track.getTitle());
    }
}
