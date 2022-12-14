package ru.kpfu.itis.shkalin.simplifytorrent.dto;

import java.util.Objects;

public class MinFileInfoDTO {
    private Integer id;
    private String title;
    private Integer fileSizeBytes;

    public MinFileInfoDTO() {
    }

    public MinFileInfoDTO(Integer id, String title, Integer fileSizeBytes) {
        this.id = id;
        this.title = title;
        this.fileSizeBytes = fileSizeBytes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(Integer fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinFileInfoDTO)) return false;
        MinFileInfoDTO that = (MinFileInfoDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(fileSizeBytes, that.fileSizeBytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, fileSizeBytes);
    }

    @Override
    public String toString() {
        return this.title;
    }
}
