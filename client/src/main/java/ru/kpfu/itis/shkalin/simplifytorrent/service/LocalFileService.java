package ru.kpfu.itis.shkalin.simplifytorrent.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;

import org.apache.commons.codec.digest.DigestUtils;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.MinFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.service.converter.ConverterService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalFileService {

    private String pathToCsv =
            "C:" + File.separator
            + "Users" + File.separator
            + "Vyash" + File.separator
            + "Desktop" + File.separator;
    private String nameOfCsv = "paths-to-uploading-files.csv";

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
                BufferedWriter bufferedWriter =
                        new BufferedWriter(
                                new FileWriter(pathToCsv + nameOfCsv, true));
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

    public void readWithFile() {
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToCsv + nameOfCsv));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitLine = line.substring(1, line.length() - 1).split("\",\"");
                LocalFileInfoDTO fileInfo = new LocalFileInfoDTO();
                fileInfo.setHashMD5(splitLine[0]);
                fileInfo.setTitle(splitLine[1]);
                fileInfo.setFileLength(Long.parseLong(splitLine[2]));
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
            File inputFile = new File(pathToCsv + nameOfCsv);
            File newFile = new File(pathToCsv + "new-file.csv");

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
                localFilesList.removeIf(localFile -> localFile.getHashMD5().equals(hashMD5));
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

    public Catalog getLocalCatalog() {
        final Object[] keys = localFilesMap.keySet().toArray();

        final ConverterService converterService = (ConverterService) AppContext.getInstance().get("converterService");
        Catalog catalog = new Catalog();
        for (Object key : keys) {
            Info info = new Info();
            converterService.update(localFilesMap.get(key.toString()), info);
            info.setPieceLength(PiecesService.PIECE_SIZE);
            catalog.put(info.getHashMD5(), info);
        }
        return catalog;
    }

    public void updateServerCatalog(Catalog catalog) {

        AppContext.getInstance().catalogFilesData.clear();
        ConverterService converter = (ConverterService) AppContext.getInstance().get("converterService");

        Object[] keysOfDownloadedCatalog = catalog.keySet().toArray();

        List<MinFileInfoDTO> newDtoList = new ArrayList<>();
        for (Object catalogKey : keysOfDownloadedCatalog) {

            MinFileInfoDTO newDto = new MinFileInfoDTO();
            Info info = catalog.get(catalogKey.toString());
            converter.update(info, newDto);
            newDtoList.add(newDto);
        }
        AppContext.getInstance().catalogFilesData.addAll(newDtoList);
    }

}
