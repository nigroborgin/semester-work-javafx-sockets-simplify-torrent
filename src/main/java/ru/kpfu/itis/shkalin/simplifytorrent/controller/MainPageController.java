package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.fxml.FXML;
import ru.kpfu.itis.shkalin.simplifytorrent.util.UploaderUtil;

public class MainPageController {

    @FXML
    private void initialize() {
    }

    public MainPageController() {
    }

    @FXML
    public void uploadButtonClicked() {
        UploaderUtil.getInstance().upload();
    }
}
