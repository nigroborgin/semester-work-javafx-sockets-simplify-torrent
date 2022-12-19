package ru.kpfu.itis.shkalin.simplifytorrent.dto;

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
    public String toString() {
        return title;
    }
}
