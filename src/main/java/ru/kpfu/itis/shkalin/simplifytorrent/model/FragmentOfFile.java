package ru.kpfu.itis.shkalin.simplifytorrent.model;

import java.util.Arrays;
import java.util.Objects;

public class FragmentOfFile {
    private Integer number;
    private Integer controlSum;
    private byte[] bytes;

    public FragmentOfFile(Integer number, Integer controlSum, byte[] bytes) {
        this.number = number;
        this.controlSum = controlSum;
        this.bytes = bytes;
    }

    public FragmentOfFile() {
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getControlSum() {
        return controlSum;
    }

    public void setControlSum(Integer controlSum) {
        this.controlSum = controlSum;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FragmentOfFile)) return false;
        FragmentOfFile that = (FragmentOfFile) o;
        return Objects.equals(number, that.number) && Objects.equals(controlSum, that.controlSum) && Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(number, controlSum);
        result = 31 * result + Arrays.hashCode(bytes);
        return result;
    }
}
