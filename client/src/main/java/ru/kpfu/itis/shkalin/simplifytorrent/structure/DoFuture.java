package ru.kpfu.itis.shkalin.simplifytorrent.structure;

@FunctionalInterface
public interface DoFuture<T> {
    void doNext(T t);
}
