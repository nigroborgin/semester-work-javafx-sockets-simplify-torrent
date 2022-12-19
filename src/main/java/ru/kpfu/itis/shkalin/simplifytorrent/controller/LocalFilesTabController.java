package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.service.FileInfoService;

public class LocalFilesTabController {

    public ObservableList<LocalFileInfoDTO> localFilesData;

    @FXML
    public ListView<LocalFileInfoDTO> localFilesListView;
    @FXML
    public Label localFilesItemTitle;
    @FXML
    public Label localFilesItemSize;
    @FXML
    public VBox localFilesVBox;

    @FXML
    public void initialize() {
        localFilesData = FileInfoService.getInstance().getLocalFilesList();

        localFilesListView.setItems(localFilesData);
        localFilesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends LocalFileInfoDTO> observable, LocalFileInfoDTO oldValue, LocalFileInfoDTO newValue) {
                if (localFilesListView.getSelectionModel().getSelectedItem() != null) {
                    localFilesItemTitle.setText(localFilesListView.getSelectionModel().getSelectedItem().getTitle());
                    localFilesItemSize.setText(localFilesListView.getSelectionModel().getSelectedItem().getFileSizeBytes().toString());
                    localFilesVBox.visibleProperty().set(true);
                } else {
                    localFilesVBox.visibleProperty().set(false);
                }
            }
        });
    }

    @FXML
    public void uploadButtonClicked() {
        System.out.println("User Files Tab: UPLOAD button clicked");
        FileInfoService.getInstance().upload();
    }

    @FXML
    public void deleteButtonClicked() {
        System.out.println("User Files Tab: DELETE button clicked");
        FileInfoService.getInstance().delete(localFilesListView.getSelectionModel().getSelectedItem().getFileHash());
    }

    public LocalFilesTabController() {
    }
}
