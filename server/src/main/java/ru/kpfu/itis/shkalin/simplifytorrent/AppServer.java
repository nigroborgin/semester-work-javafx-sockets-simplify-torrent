package ru.kpfu.itis.shkalin.simplifytorrent;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.MultiThreadServer;

public class AppServer {
    private static final int PORT = 1234;
    private static final int COUNT_OF_THREADS = 5;

    public static void main(String[] args) {
        try {
            MultiThreadServer server = new MultiThreadServer(COUNT_OF_THREADS, PORT);
            server.startServer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
