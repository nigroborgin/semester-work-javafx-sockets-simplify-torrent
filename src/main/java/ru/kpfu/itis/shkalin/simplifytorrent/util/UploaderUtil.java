package ru.kpfu.itis.shkalin.simplifytorrent.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kpfu.itis.shkalin.simplifytorrent.service.PathsFileService;

import java.io.File;
import java.util.List;

public class UploaderUtil {

    private static volatile UploaderUtil instance;

    public static UploaderUtil getInstance() {
        UploaderUtil localInstance = instance;
        if (localInstance == null) {
            synchronized (UploaderUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UploaderUtil();
                }
            }
        }
        return localInstance;
    }

    private UploaderUtil() {
    }

    public void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file(s) for uploading");
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        if (list != null) {
            for (File file : list) {
                PathsFileService.getInstance()
                        .writeNewFileInfo(file);
            }
        }
    }
}
