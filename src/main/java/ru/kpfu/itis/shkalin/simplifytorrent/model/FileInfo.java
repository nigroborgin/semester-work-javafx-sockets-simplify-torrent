package ru.kpfu.itis.shkalin.simplifytorrent.model;

import java.util.Objects;

public class FileInfo {
    private String fileHash;
    private String title;
    private Long fileSizeBytes;
    private String filePath;

    public FileInfo() {
    }

    public FileInfo(String fileHash, String title, Long fileSizeBytes, String filePath) {
        this.fileHash = fileHash;
        this.title = title;
        this.fileSizeBytes = fileSizeBytes;
        this.filePath = filePath;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(fileHash, fileInfo.fileHash) && Objects.equals(title, fileInfo.title) && Objects.equals(fileSizeBytes, fileInfo.fileSizeBytes) && Objects.equals(filePath, fileInfo.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileHash, title, fileSizeBytes, filePath);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileHash='" + fileHash + '\'' +
                ", title='" + title + '\'' +
                ", fileSizeBytes=" + fileSizeBytes +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
