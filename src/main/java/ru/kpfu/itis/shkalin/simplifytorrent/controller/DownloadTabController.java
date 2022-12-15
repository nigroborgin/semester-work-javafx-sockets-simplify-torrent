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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.TorrentFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.service.PathsFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.util.UploaderUtil;

import java.io.File;
import java.util.List;

public class DownloadTabController {

    public static ObservableList<TorrentFileInfoDTO> downloadsFilesData = FXCollections.observableArrayList();

    @FXML
    public TableView<TorrentFileInfoDTO> tableView;
    @FXML
    public TableColumn<TorrentFileInfoDTO, String> titleColumn;
    @FXML
    public TableColumn<TorrentFileInfoDTO, Integer> fileSizeColumn;
    @FXML
    public TableColumn<TorrentFileInfoDTO, Integer> progressColumn;
    @FXML
    public TableColumn<TorrentFileInfoDTO, String> statusColumn;
    @FXML
    public TableColumn<TorrentFileInfoDTO, Integer> downloadedColumn;
    @FXML
    public TableColumn<TorrentFileInfoDTO, Integer> uploadedColumn;
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

        downloadsFilesData.add(new TorrentFileInfoDTO(1,"Пиратский Варкрафт", 24000, 62, "downloading", 15000, 1000));
        downloadsFilesData.add(new TorrentFileInfoDTO(2, "no man's sky pirates", 24000, 100, "uploading", 24000, 16000));
        downloadsFilesData.add(new TorrentFileInfoDTO(3, "Властелин колец 1 !!!", 10000, 35, "downloading", 3500, 1000));

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends TorrentFileInfoDTO> observable, TorrentFileInfoDTO oldValue, TorrentFileInfoDTO newValue) {
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
        System.out.println("Download Tab: UPLOAD button clicked");
        UploaderUtil.getInstance().upload();
    }

    @FXML
    private void playButtonClicked() {
        System.out.println("Download Tab: PLAY button clicked");
    }

    @FXML
    private void pauseButtonClicked() {
        System.out.println("Download Tab: PAUSE button clicked");
    }

    @FXML
    private void stopButtonClicked() {
        System.out.println("Download Tab: STOP button clicked");
    }

    @FXML
    public void deleteButtonClicked() {
        System.out.println("Download Tab: DELETE button clicked");
    }

    public DownloadTabController() {
    }
}
