package ru.kpfu.itis.shkalin.simplifytorrent.entity;

import java.io.Serializable;
import java.util.Objects;

public class Info implements Serializable {

    private String hashMD5;
    private Integer pieceLength;
    private Long fileLength;
    private String title;

    public Info() {

    }

    public Info(String hashMD5, Integer pieceLength, Long fileLength, String title) {
        this.hashMD5 = hashMD5;
        this.pieceLength = pieceLength;
        this.fileLength = fileLength;
        this.title = title;
    }

    public Info(String hashMD5, String title, Long fileLength) {
        this.hashMD5 = hashMD5;
        this.title = title;
        this.fileLength = fileLength;
    }

    public Info(String hashMD5) {
        this.hashMD5 = hashMD5;
    }

    public String getHashMD5() {
        return hashMD5;
    }

    public void setHashMD5(String hashMD5) {
        this.hashMD5 = hashMD5;
    }

    public Integer getPieceLength() {
        return pieceLength;
    }

    public void setPieceLength(Integer pieceLength) {
        this.pieceLength = pieceLength;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Info)) return false;
        Info info = (Info) o;
        return Objects.equals(hashMD5, info.hashMD5) && Objects.equals(pieceLength, info.pieceLength) && Objects.equals(fileLength, info.fileLength) && Objects.equals(title, info.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashMD5, pieceLength, fileLength, title);
    }

    @Override
    public String toString() {
        return "Info{" +
                "hashMD5='" + hashMD5 + '\'' +
                ", pieceLength=" + pieceLength +
                ", fileLength=" + fileLength +
                ", title='" + title + '\'' +
                '}';
    }

}
