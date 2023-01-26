package ru.kpfu.itis.shkalin.simplifytorrent.service;

import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;

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

    public Info head() {
        Info infoOfFile = null;

        String hashMD5;
        Integer pieceLength;
        String piecesHashesSHA1;
        Long fileLength;
        String title;

        return infoOfFile;
    }

    public void get(String fullFileHashMD5, Integer idPiece) {
//        System.out.println("CommunicationWithServerService: get (id: " + fileId + ") file");
    }

    public void post(Piece piece) {

    }


}