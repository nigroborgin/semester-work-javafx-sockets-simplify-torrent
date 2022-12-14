module ru.kpfu.itis.shkalin.simplifytorrent {

    requires javafx.base;
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.kpfu.itis.shkalin.simplifytorrent;
    opens ru.kpfu.itis.shkalin.simplifytorrent.controller;
    opens ru.kpfu.itis.shkalin.simplifytorrent.dto;
    opens ru.kpfu.itis.shkalin.simplifytorrent.model;
    opens ru.kpfu.itis.shkalin.simplifytorrent.service;

    exports ru.kpfu.itis.shkalin.simplifytorrent;
    exports ru.kpfu.itis.shkalin.simplifytorrent.controller;
    exports ru.kpfu.itis.shkalin.simplifytorrent.dto;
    exports ru.kpfu.itis.shkalin.simplifytorrent.model;
    exports ru.kpfu.itis.shkalin.simplifytorrent.service;
}