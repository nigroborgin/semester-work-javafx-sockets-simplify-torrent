package ru.kpfu.itis.shkalin.simplifytorrent.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.MinFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.service.ServerService;
import ru.kpfu.itis.shkalin.simplifytorrent.util.UploaderUtil;

public class CatalogTabController {

    public static ObservableList<MinFileInfoDTO> catalogFilesData = FXCollections.observableArrayList();
    public ServerService communication = ServerService.getInstance();

    @FXML
    public ListView<MinFileInfoDTO> catalogListView;
    @FXML
    public Label catalogItemTitle;
    @FXML
    public Label catalogItemSize;
    @FXML
    public VBox catalogVBox;

    @FXML
    public void initialize() {

        catalogFilesData.add(new MinFileInfoDTO(1, "Пиратский Варкрафт", 24000));
        catalogFilesData.add(new MinFileInfoDTO(2, "Dark Souls", 10000));
        catalogFilesData.add(new MinFileInfoDTO(3, "Пиратский mine", 12000));
        catalogFilesData.add(new MinFileInfoDTO(4, "Spirit of the North", 45702));
        catalogListView.setItems(catalogFilesData);
        catalogListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends MinFileInfoDTO> observable, MinFileInfoDTO oldValue, MinFileInfoDTO newValue) {
                catalogItemTitle.setText(catalogListView.getSelectionModel().getSelectedItem().getTitle());
                catalogItemSize.setText(catalogListView.getSelectionModel().getSelectedItem().getFileSizeBytes().toString());
                catalogVBox.visibleProperty().set(true);
            }
        });
    }

    @FXML
    public void downloadButtonClicked() {
        System.out.println("Catalog Tab: DOWNLOAD button clicked");
        communication.get(catalogListView.getSelectionModel().getSelectedItem().getId());
    }

    @FXML
    public void uploadButtonClicked() {
        System.out.println("Catalog Tab: UPLOAD button clicked");
        UploaderUtil.getInstance().upload();
    }

    public CatalogTabController() {
    }
}
