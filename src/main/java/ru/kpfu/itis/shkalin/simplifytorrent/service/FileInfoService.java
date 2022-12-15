package ru.kpfu.itis.shkalin.simplifytorrent.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kpfu.itis.shkalin.simplifytorrent.model.FileInfo;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.List;

public class FileInfoService {

    private static volatile FileInfoService instance;
    private ObservableList<FileInfo> localFilesList;

    private FileInfoService() {
        localFilesList = FXCollections.observableArrayList();
        readWithFile();
    }

    public static FileInfoService getInstance() {
        FileInfoService localInstance = instance;
        if (localInstance == null) {
            synchronized (FileInfoService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FileInfoService();
                }
            }
        }
        return localInstance;
    }

    public ObservableList<FileInfo> getLocalFilesList() {
        return localFilesList;
    }

    public void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file(s) for uploading");
        List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
        if (list != null) {
            for (File file : list) {
                writeNewFileInfo(file);
            }
        }
    }

    public void writeNewFileInfo(File file) {
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            String hashMD5 = DigestUtils.md5Hex(inputStream);
            String title = file.getName();
            Long fileSizeBytes = file.length();
            String filePath = file.getAbsolutePath();
            inputStream.close();

            localFilesList.add(new FileInfo(hashMD5, title, fileSizeBytes, filePath));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv", true));
            bufferedWriter.append("\"")
                    .append(hashMD5).append("\",\"")
                    .append(title).append("\",\"")
                    .append(String.valueOf(fileSizeBytes)).append("\",\"")
                    .append(filePath).append("\"\n");
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readWithFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitLine = line.substring(1, line.length() - 1).split("\",\"");
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileHash(splitLine[0]);
                fileInfo.setTitle(splitLine[1]);
                fileInfo.setFileSizeBytes(Long.parseLong(splitLine[2]));
                fileInfo.setFilePath(splitLine[3]);
                localFilesList.add(fileInfo);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String hash) {
        try {
            File inputFile = new File("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv");
            File newFile = new File("target/classes/ru/kpfu/itis/shkalin/simplifytorrent/new-file.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(hash)) {
                    writer.append(line + "\n");
                }
            }

            reader.close();
            writer.close();
            inputFile.delete();
            newFile.renameTo(inputFile);

            if (!localFilesList.isEmpty()) {
                localFilesList.removeIf(localFile -> localFile.getFileHash().equals(hash));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
