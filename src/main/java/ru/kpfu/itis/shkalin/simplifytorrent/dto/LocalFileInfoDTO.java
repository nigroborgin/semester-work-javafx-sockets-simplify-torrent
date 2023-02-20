package ru.kpfu.itis.shkalin.simplifytorrent.dto;

import java.util.Objects;

public class LocalFileInfoDTO {

    private String fileHash;
    private String title;
    private Long fileSizeBytes;
    private String fileLocalPath;

    public LocalFileInfoDTO() {

    }

    public LocalFileInfoDTO(String fileHash, String title, Long fileSizeBytes, String fileLocalPath) {
        this.fileHash = fileHash;
        this.title = title;
        this.fileSizeBytes = fileSizeBytes;
        this.fileLocalPath = fileLocalPath;
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

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalFileInfoDTO)) return false;
        LocalFileInfoDTO that = (LocalFileInfoDTO) o;
        return Objects.equals(fileHash, that.fileHash) && Objects.equals(title, that.title) && Objects.equals(fileSizeBytes, that.fileSizeBytes) && Objects.equals(fileLocalPath, that.fileLocalPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileHash, title, fileSizeBytes, fileLocalPath);
    }

    @Override
    public String toString() {
        return title;
    }
}
