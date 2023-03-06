package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.DownloadingFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.UploadService;

import java.util.List;

public class DownloadTabController {

    public static ObservableList<DownloadingFileInfoDTO> downloadsFilesData = FXCollections.observableArrayList();

    @FXML
    public TableView<DownloadingFileInfoDTO> tableView;
    @FXML
    public TableColumn<DownloadingFileInfoDTO, String> titleColumn;
    @FXML
    public TableColumn<DownloadingFileInfoDTO, Integer> fileSizeColumn;
    @FXML
    public TableColumn<DownloadingFileInfoDTO, Integer> progressColumn;
    @FXML
    public TableColumn<DownloadingFileInfoDTO, String> statusColumn;
    @FXML
    public TableColumn<DownloadingFileInfoDTO, Integer> downloadedColumn;
    @FXML
    public TableColumn<DownloadingFileInfoDTO, Integer> uploadedColumn;
    @FXML
    public Button deleteButton;

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSizeBytes"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        downloadedColumn.setCellValueFactory(new PropertyValueFactory<>("downloadedBytes"));
        uploadedColumn.setCellValueFactory(new PropertyValueFactory<>("uploadedBytes"));

        downloadsFilesData.add(new DownloadingFileInfoDTO(1, "Пиратский Варкрафт", 24000, 62, "downloading", 15000, 1000));
        downloadsFilesData.add(new DownloadingFileInfoDTO(2, "no man's sky pirates", 24000, 100, "uploading", 24000, 16000));
        downloadsFilesData.add(new DownloadingFileInfoDTO(3, "Властелин колец 1 !!!", 10000, 35, "downloading", 3500, 1000));

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends DownloadingFileInfoDTO> observable, DownloadingFileInfoDTO oldValue, DownloadingFileInfoDTO newValue) {
                if (tableView.getSelectionModel().getSelectedItem().getStatus().equals("downloading")) {
                    deleteButton.setText("Cancel download");
                } else if (tableView.getSelectionModel().getSelectedItem().getStatus().equals("uploading")) {
                    deleteButton.setText("Delete from list");
                }
                deleteButton.setVisible(true);
            }
        });

        tableView.setItems(downloadsFilesData);
    }

    @FXML
    public void uploadButtonClicked() {
        System.out.println("\nDownload Tab: UPLOAD button clicked");
        List<LocalFileInfoDTO> additionalFilesList =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                        .addFiles();

        ((UploadService) AppContext.getInstance().get("uploadService"))
                .uploadCatalog(additionalFilesList);
    }

    @FXML
    private void playButtonClicked() {
        System.out.println("\nDownload Tab: PLAY button clicked");
    }

    @FXML
    private void pauseButtonClicked() {
        System.out.println("\nDownload Tab: PAUSE button clicked");
    }

    @FXML
    private void stopButtonClicked() {
        System.out.println("\nDownload Tab: STOP button clicked");
    }

    @FXML
    public void deleteButtonClicked() {
        System.out.println("\nDownload Tab: DELETE button clicked");
    }

    public DownloadTabController() {
    }
}
