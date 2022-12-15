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
import ru.kpfu.itis.shkalin.simplifytorrent.util.UploaderUtil;

public class LocalFilesTabController {

    public static ObservableList<MinFileInfoDTO> localFilesData = FXCollections.observableArrayList();

    @FXML
    public ListView<MinFileInfoDTO> localFilesListView;
    @FXML
    public Label localFilesItemTitle;
    @FXML
    public Label localFilesItemSize;
    @FXML
    public VBox localFilesVBox;

    @FXML
    public void initialize() {

        localFilesData.add(new MinFileInfoDTO(2, "МОЙ Dark Souls", 10000));
        localFilesData.add(new MinFileInfoDTO(3, "МОЙ Пиратский mine", 12000));
        localFilesData.add(new MinFileInfoDTO(1, "МОЙ Пиратский Варкрафт", 24000));
        localFilesData.add(new MinFileInfoDTO(4, "МОЙ Spirit of the North", 45702));
        localFilesListView.setItems(localFilesData);
        localFilesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends MinFileInfoDTO> observable, MinFileInfoDTO oldValue, MinFileInfoDTO newValue) {
                localFilesItemTitle.setText(localFilesListView.getFocusModel().getFocusedItem().getTitle());
                localFilesItemSize.setText(localFilesListView.getFocusModel().getFocusedItem().getFileSizeBytes().toString());
                localFilesVBox.visibleProperty().set(true);
            }
        });
    }

    @FXML
    public void uploadButtonClicked() {
        System.out.println("User Files Tab: UPLOAD button clicked");
        UploaderUtil.getInstance().upload();
    }

    @FXML
    public void deleteButtonClicked() {
        System.out.println("User Files Tab: DELETE button clicked");
    }

    public LocalFilesTabController() {
    }
}
