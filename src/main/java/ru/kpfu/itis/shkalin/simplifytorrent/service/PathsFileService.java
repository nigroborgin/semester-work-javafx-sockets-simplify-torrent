package ru.kpfu.itis.shkalin.simplifytorrent.service;

import ru.kpfu.itis.shkalin.simplifytorrent.model.FileInfo;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PathsFileService {

    private static volatile PathsFileService instance;
    private List<FileInfo> fileInfoList;

    private PathsFileService() {
        fileInfoList = new ArrayList<>();
        readWithFile();
    }

    public static PathsFileService getInstance() {
        PathsFileService localInstance = instance;
        if (localInstance == null) {
            synchronized (PathsFileService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PathsFileService();
                }
            }
        }
        return localInstance;
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void writeNewFileInfo(File file) {

        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())) {
            String hashMD5 = DigestUtils.md5Hex(inputStream);
            String title = file.getName();
            Long fileSizeBytes = file.length();
            String filePath = file.getAbsolutePath();
            inputStream.close();

            fileInfoList.add(new FileInfo(hashMD5, title, fileSizeBytes, filePath));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                    getClass().getResource("/ru/kpfu/itis/shkalin/simplifytorrent/paths-to-uploading-files.csv").getFile()));
            bufferedWriter.write(hashMD5+','+title+','+fileSizeBytes+','+filePath+'\n');
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readWithFile() {
        // ...
    }

    public void delete() {

    }
}
