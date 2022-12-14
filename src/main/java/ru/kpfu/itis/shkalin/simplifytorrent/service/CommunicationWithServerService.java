package ru.kpfu.itis.shkalin.simplifytorrent.service;

import java.io.File;

public class CommunicationWithServerService {

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
