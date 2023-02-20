package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.UploadService;

import java.util.List;

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
    public void initialize() throws ClientException {
        localFilesData =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                        .getLocalFilesList();
        ((UploadService) AppContext.getInstance().get("uploadService"))
                .uploadCatalog(localFilesData);

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
    public void uploadButtonClicked() throws ClientException {
        System.out.println("User Files Tab: UPLOAD button clicked");
        List<LocalFileInfoDTO> additionalFilesList =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                        .addFiles();

        if (!additionalFilesList.isEmpty()) {
            ((UploadService) AppContext.getInstance().get("uploadService"))
                    .uploadCatalog(additionalFilesList);
        }
    }

    @FXML
    public void deleteButtonClicked() {
        System.out.println("User Files Tab: DELETE button clicked");
        ((LocalFileService) AppContext.getInstance().get("localFileService"))
                .delete(localFilesListView.getSelectionModel().getSelectedItem().getFileHash());
    }

    public LocalFilesTabController() {
    }
}
