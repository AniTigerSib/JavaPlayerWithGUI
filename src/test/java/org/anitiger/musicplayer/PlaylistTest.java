package org.anitiger.musicplayer;

import org.anitiger.musicplayer.track.containers.Playlist;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;

public class PlaylistTest {
    @Test
    public void testGetTitle_defaultTitle() {
        Playlist playlist = new Playlist();
        assertEquals("New Playlist", playlist.getTitle());
    }

    @Test
    public void testGetTitle_withTitle() {
        Playlist playlist = new Playlist("My Playlist");
        assertEquals("My Playlist", playlist.getTitle());
    }
}
