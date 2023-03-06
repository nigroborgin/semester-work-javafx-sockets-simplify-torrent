package ru.kpfu.itis.shkalin.simplifytorrent.dto;

import java.util.Objects;

public class LocalFileInfoDTO {

    private String hashMD5;
    private String title;
    private Long fileLength;
    private String fileLocalPath;

    public LocalFileInfoDTO() {

    }

    public LocalFileInfoDTO(String hashMD5, String title, Long fileLength, String fileLocalPath) {
        this.hashMD5 = hashMD5;
        this.title = title;
        this.fileLength = fileLength;
        this.fileLocalPath = fileLocalPath;
    }

    public String getHashMD5() {
        return hashMD5;
    }

    public void setHashMD5(String hashMD5) {
        this.hashMD5 = hashMD5;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
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
        return Objects.equals(hashMD5, that.hashMD5) && Objects.equals(title, that.title) && Objects.equals(fileLength, that.fileLength) && Objects.equals(fileLocalPath, that.fileLocalPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMD5, title, fileLength, fileLocalPath);
    }

    @Override
    public String toString() {
        return title;
    }
}
