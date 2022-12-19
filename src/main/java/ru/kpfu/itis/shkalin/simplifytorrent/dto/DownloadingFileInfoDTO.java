package ru.kpfu.itis.shkalin.simplifytorrent.dto;

import java.util.Objects;

public class DownloadingFileInfoDTO {

    private Integer id;
    private String title;
    private Integer fileSizeBytes;
    private Integer progress;
    private String status;
    private Integer downloadedBytes;
    private Integer uploadedBytes;

    public DownloadingFileInfoDTO(Integer id, String title, Integer fileSizeBytes, Integer progress, String status, Integer downloadedBytes, Integer uploadedBytes) {
        this.id = id;
        this.title = title;
        this.fileSizeBytes = fileSizeBytes;
        this.progress = progress;
        this.status = status;
        this.downloadedBytes = downloadedBytes;
        this.uploadedBytes = uploadedBytes;
    }

    public DownloadingFileInfoDTO() {
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

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(Integer downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public Integer getUploadedBytes() {
        return uploadedBytes;
    }

    public void setUploadedBytes(Integer uploadedBytes) {
        this.uploadedBytes = uploadedBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadingFileInfoDTO)) return false;
        DownloadingFileInfoDTO that = (DownloadingFileInfoDTO) o;
        return Objects.equals(title, that.title) && Objects.equals(fileSizeBytes, that.fileSizeBytes) && Objects.equals(progress, that.progress) && Objects.equals(status, that.status) && Objects.equals(downloadedBytes, that.downloadedBytes) && Objects.equals(uploadedBytes, that.uploadedBytes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, fileSizeBytes, progress, status, downloadedBytes, uploadedBytes);
    }

    @Override
    public String toString() {
        return "TorrentFileInfoDTO{" +
                "title='" + title + '\'' +
                ", fileSizeBytes=" + fileSizeBytes +
                ", progress=" + progress +
                ", status='" + status + '\'' +
                ", downloadedBytes=" + downloadedBytes +
                ", uploadedBytes=" + uploadedBytes +
                '}';
    }
}
