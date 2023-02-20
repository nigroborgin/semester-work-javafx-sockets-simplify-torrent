package ru.kpfu.itis.shkalin.simplifytorrent.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalFileService {

    private ObservableList<LocalFileInfoDTO> localFilesList;
    private Map<String, LocalFileInfoDTO> localFilesMap;

    public LocalFileService() {
        localFilesList = FXCollections.observableArrayList();
        localFilesMap = new HashMap<>();
        readWithFile();
    }

    public ObservableList<LocalFileInfoDTO> getLocalFilesList() {
        return localFilesList;
    }

    public Map<String, LocalFileInfoDTO> getLocalFilesMap() {
        return localFilesMap;
    }

    public List<LocalFileInfoDTO> addFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file(s) for uploading");
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        List<LocalFileInfoDTO> additionalFilesList = new ArrayList<>();
        if (list != null) {
            for (File file : list) {
                LocalFileInfoDTO localFileInfoDTO = writeNewFileInfo(file);
                if (localFileInfoDTO != null) {
                    additionalFilesList.add(localFileInfoDTO);
                }
            }
        }
        return additionalFilesList;
    }

    public LocalFileInfoDTO writeNewFileInfo(File file) {
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            String hashMD5 = DigestUtils.md5Hex(inputStream);
            if (!getLocalFilesMap().containsKey(hashMD5)) {
                String title = file.getName();
                Long fileSizeBytes = file.length();
                String filePath = file.getAbsolutePath();
                inputStream.close();

                LocalFileInfoDTO localFileInfoDTO = new LocalFileInfoDTO(hashMD5, title, fileSizeBytes, filePath);
                localFilesList.add(localFileInfoDTO);
                localFilesMap.put(hashMD5, localFileInfoDTO);
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv", true));
                bufferedWriter.append("\"")
                        .append(hashMD5).append("\",\"")
                        .append(title).append("\",\"")
                        .append(String.valueOf(fileSizeBytes)).append("\",\"")
                        .append(filePath).append("\"\n");
                bufferedWriter.flush();
                bufferedWriter.close();
                return localFileInfoDTO;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readWithFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitLine = line.substring(1, line.length() - 1).split("\",\"");
                LocalFileInfoDTO fileInfo = new LocalFileInfoDTO();
                fileInfo.setFileHash(splitLine[0]);
                fileInfo.setTitle(splitLine[1]);
                fileInfo.setFileSizeBytes(Long.parseLong(splitLine[2]));
                fileInfo.setFileLocalPath(splitLine[3]);

                localFilesList.add(fileInfo);
                localFilesMap.put(splitLine[0], fileInfo);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String hashMD5) {
        try {
            File inputFile = new File("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv");
            File newFile = new File("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/new-file.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(hashMD5)) {
                    writer.append(line + "\n");
                }
            }

            reader.close();
            writer.close();
            inputFile.delete();
            newFile.renameTo(inputFile);

            if (!localFilesList.isEmpty()) {
                localFilesList.removeIf(localFile -> localFile.getFileHash().equals(hashMD5));
            }
            if (!localFilesMap.isEmpty()) {
                localFilesMap.remove(hashMD5);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
