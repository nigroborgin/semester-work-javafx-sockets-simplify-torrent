module ru.kpfu.itis.shkalin.simplifytorrent {

    requires javafx.base;
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;

    opens ru.kpfu.itis.shkalin.simplifytorrent;
    opens ru.kpfu.itis.shkalin.simplifytorrent.controller;
    opens ru.kpfu.itis.shkalin.simplifytorrent.dto;
    opens ru.kpfu.itis.shkalin.simplifytorrent.entity;
    opens ru.kpfu.itis.shkalin.simplifytorrent.service;
    opens ru.kpfu.itis.shkalin.simplifytorrent.protocol;
    opens ru.kpfu.itis.shkalin.simplifytorrent.protocol.message;
    opens ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields;
    opens ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception;

    exports ru.kpfu.itis.shkalin.simplifytorrent;
    exports ru.kpfu.itis.shkalin.simplifytorrent.controller;
    exports ru.kpfu.itis.shkalin.simplifytorrent.dto;
    exports ru.kpfu.itis.shkalin.simplifytorrent.entity;
    exports ru.kpfu.itis.shkalin.simplifytorrent.service;
    exports ru.kpfu.itis.shkalin.simplifytorrent.protocol;
    exports ru.kpfu.itis.shkalin.simplifytorrent.protocol.message;
    exports ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields;
    exports ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception;

}