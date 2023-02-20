package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.MinFileInfoDTO;

import ru.kpfu.itis.shkalin.simplifytorrent.entity.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.CheckStatusMessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.MessageException;

import ru.kpfu.itis.shkalin.simplifytorrent.service.DownloadService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.UploadService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.converter.ConverterService;

import java.io.IOException;
import java.util.List;

public class CatalogTabController {

    public ObservableList<MinFileInfoDTO> catalogFilesData = FXCollections.observableArrayList();

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
        try {
            updateCatalog();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (CheckStatusMessageException e) {
            handleCheckMessageStatus(e);
        } catch (MessageException e) {
            catalogItemStatus.setText("Error: unknown message");
        }

        catalogListView.setItems(catalogFilesData);
        catalogListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends MinFileInfoDTO> observable, MinFileInfoDTO oldValue, MinFileInfoDTO newValue) {
                catalogItemTitle.setText(catalogListView.getSelectionModel().getSelectedItem().getTitle());
                catalogItemSize.setText(catalogListView.getSelectionModel().getSelectedItem().getFileLength().toString());
                catalogVBox.visibleProperty().set(true);
            }
        });
    }



    @FXML
    public void downloadButtonClicked() {
        System.out.println("Catalog Tab: DOWNLOAD button clicked");
        // TODO: visible downloading
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
    public void uploadButtonClicked() throws ClientException {
        System.out.println("Catalog Tab: UPLOAD button clicked");
        List<LocalFileInfoDTO> additionalFilesList =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                        .addFiles();

        ((UploadService) AppContext.getInstance().get("uploadService"))
                .uploadCatalog(additionalFilesList);
    }

    @FXML
    public void updateCatalogButtonClicked() {
        try {
            updateCatalog();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (CheckStatusMessageException e) {
            handleCheckMessageStatus(e);
        } catch (MessageException e) {
            catalogItemStatus.setText("Error: unknown message");
        }
    }

    public CatalogTabController() {

    }

    private void updateCatalog() throws ClientException, MessageException {
        Catalog catalog = ((DownloadService) AppContext.getInstance().get("downloadService"))
                .downloadCatalog();
        ConverterService converter = (ConverterService) AppContext.getInstance().get("converterService");

        Object[] keysOfDownloadedCatalog = catalog.keySet().toArray();
        boolean isEqual;

        for (Object catalogKey : keysOfDownloadedCatalog) {
            isEqual = false;

            for (MinFileInfoDTO dto : catalogFilesData) {

                if (catalogKey.toString().equals(dto.getHashMD5())) {
                    isEqual = true;
                    break;
                }
            }
            if (!isEqual) {
                MinFileInfoDTO newDto = new MinFileInfoDTO();
                Info info = catalog.get(catalogKey.toString());
                converter.update(info, newDto);
                catalogFilesData.add(newDto);
            }
        }
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
