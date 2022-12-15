package ru.kpfu.itis.shkalin.simplifytorrent.service;

import java.io.File;

public class ServerService {
    private static volatile ServerService instance;

    public static ServerService getInstance() {
        ServerService localInstance = instance;
        if (localInstance == null) {
            synchronized (ServerService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ServerService();
                }
            }
        }
        return localInstance;
    }

    private ServerService() {
    }

    public void head() {
        Integer id;
        String title;
        Integer fileSizeBytes;
    }

    public void get(Integer fileId) {
        System.out.println("CommunicationWithServerService: get (id: " + fileId + ") file");
    }

    public void post(File file) {

    }

}
