module org.anitiger.musicplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.slf4j;
    requires javafx.media;

    exports org.anitiger.musicplayer.controllers;
    opens org.anitiger.musicplayer.controllers to javafx.fxml;
    exports org.anitiger.musicplayer;
    opens org.anitiger.musicplayer to javafx.fxml;
    exports org.anitiger.musicplayer.track.models;
    opens org.anitiger.musicplayer.track.models to javafx.base;
}