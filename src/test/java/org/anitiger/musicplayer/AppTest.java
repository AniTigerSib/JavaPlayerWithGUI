package org.anitiger.musicplayer;


import org.anitiger.musicplayer.track.containers.Playlist;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class AppTest {
    @Test
    public void testIsPlaylistAlreadyExists_emptyPlaylist() {
        App.playlists.clear();
        assertFalse(App.isPlaylistAlreadyExists(new Playlist("Test Playlist")));
    }

    @Test
    public void testIsPlaylistAlreadyExists_existingPlaylist() {
        App.playlists.add(new Playlist("Test Playlist 1"));
        assertTrue(App.isPlaylistAlreadyExists(new Playlist("Test Playlist 1")));
    }

    @Test
    public void testIsPlaylistAlreadyExists_caseSensitive() {
        App.playlists.add(new Playlist("Test Playlist"));
        assertFalse(App.isPlaylistAlreadyExists(new Playlist("test playlist")));
    }
}
