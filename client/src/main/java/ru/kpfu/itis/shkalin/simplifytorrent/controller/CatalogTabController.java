package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.MinFileInfoDTO;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.CheckStatusMessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.MessageException;

import ru.kpfu.itis.shkalin.simplifytorrent.service.DownloadService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.UploadService;

import java.io.IOException;
import java.util.List;

public class CatalogTabController {

    public volatile ObservableList<MinFileInfoDTO> catalogFilesData;

    @FXML
    public ListView<MinFileInfoDTO> catalogListView;
    @FXML
    public Label catalogItemTitle;
    @FXML
    public Label catalogItemSize;
    @FXML
    public Label catalogItemStatus;
    @FXML
    public VBox catalogVBox;

    @FXML
    public void initialize() {
        catalogFilesData = AppContext.getInstance().catalogFilesData;
        updateCatalog();

        catalogListView.setItems(catalogFilesData);
        catalogListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            catalogItemTitle.setText(catalogListView.getSelectionModel().getSelectedItem().getTitle());
            catalogItemSize.setText(catalogListView.getSelectionModel().getSelectedItem().getFileLength().toString());
            catalogVBox.visibleProperty().set(true);
        });
    }

    @FXML
    public void downloadButtonClicked() {
        System.out.println("\nCatalog Tab: DOWNLOAD button clicked");
        try {
            ((DownloadService) AppContext.getInstance().get("downloadService"))
                    .downloadFile(catalogListView.getSelectionModel().getSelectedItem().getHashMD5());
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (CheckStatusMessageException e) {
            handleCheckMessageStatus(e);
        } catch (MessageException e) {
            catalogItemStatus.setText("Error: unknown message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void uploadButtonClicked() {
        System.out.println("\nCatalog Tab: UPLOAD button clicked");
        List<LocalFileInfoDTO> additionalFilesList =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                        .addFiles();

        ((UploadService) AppContext.getInstance().get("uploadService"))
                .uploadCatalog(additionalFilesList);
    }

    @FXML
    public void updateCatalogButtonClicked() {
        System.out.println("\nCatalog Tab: UPDATE button clicked");
        updateCatalog();
    }

    public CatalogTabController() {

    }

    private void updateCatalog() {
        ((DownloadService) AppContext.getInstance().get("downloadService"))
                .downloadCatalog();
    }

    private void handleCheckMessageStatus(CheckStatusMessageException e) {
        switch (e.getMessageStatus()) {
            case NOT_INFO_ERROR:
                catalogItemStatus.setText("Error: not info about this file on server");
                break;
            case NOT_PIECE_ERROR:
                catalogItemStatus.setText("Error: not piece of file on sids");
                break;
            case NOT_SIDS_ERROR:
                catalogItemStatus.setText("Error: not sids for getting info or piece");
                break;
            case SERVER_ERROR:
                catalogItemStatus.setText("Error on server");
                break;
        }
    }

}
