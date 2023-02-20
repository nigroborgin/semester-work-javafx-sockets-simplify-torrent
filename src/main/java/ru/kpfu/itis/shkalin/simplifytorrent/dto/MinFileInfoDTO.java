package ru.kpfu.itis.shkalin.simplifytorrent.dto;

import java.util.Objects;

public class MinFileInfoDTO {

    private String hashMD5;
    private String title;
    private Long fileLength;

    public MinFileInfoDTO() {
    }

    public MinFileInfoDTO(String hashMD5, String title, Long fileLength) {
        this.hashMD5 = hashMD5;
        this.title = title;
        this.fileLength = fileLength;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinFileInfoDTO)) return false;
        MinFileInfoDTO that = (MinFileInfoDTO) o;
        return Objects.equals(hashMD5, that.hashMD5) && Objects.equals(title, that.title) && Objects.equals(fileLength, that.fileLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMD5, title, fileLength);
    }

    @Override
    public String toString() {
        return this.title;
    }
}
