package ru.kpfu.itis.shkalin.simplifytorrent.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Piece implements Serializable {

    private String hashMD5;
    private Long id;
    private String hashSHA1;
    private byte[] blockData;

    public Piece() {

    }

    public Piece(String hashMD5, Long id, String hashSHA1, byte[] blockData) {
        this.hashMD5 = hashMD5;
        this.id = id;
        this.hashSHA1 = hashSHA1;
        this.blockData = blockData;
    }

    public Piece(String hashMD5, Long id) {
        this.hashMD5 = hashMD5;
        this.id = id;
    }

    public String getHashMD5() {
        return hashMD5;
    }

    public void setHashMD5(String hashMD5) {
        this.hashMD5 = hashMD5;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashSHA1() {
        return hashSHA1;
    }

    public void setHashSHA1(String hashSHA1) {
        this.hashSHA1 = hashSHA1;
    }

    public byte[] getBlockData() {
        return blockData;
    }

    public void setBlockData(byte[] blockData) {
        this.blockData = blockData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return Objects.equals(hashMD5, piece.hashMD5) && Objects.equals(id, piece.id) && Objects.equals(hashSHA1, piece.hashSHA1) && Arrays.equals(blockData, piece.blockData);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hashMD5, id, hashSHA1);
        result = 31 * result + Arrays.hashCode(blockData);
        return result;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "hashMD5='" + hashMD5 + '\'' +
                ", id=" + id +
                ", hashSHA1='" + hashSHA1 + '\'' +
                ", blockData=" + Arrays.toString(blockData) +
                '}';
    }
}
