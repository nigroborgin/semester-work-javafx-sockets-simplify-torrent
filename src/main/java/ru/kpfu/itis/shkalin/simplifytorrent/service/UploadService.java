package ru.kpfu.itis.shkalin.simplifytorrent.service;

import javafx.collections.ObservableList;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Client;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

import java.io.IOException;
import java.util.List;

public class UploadService {
    private Client client;

    public UploadService(Client client) {
        this.client = client;
    }

    public void uploadInfo(String hashMD5) {
        Message message = new Message(MessageType.RESPONSE, MessageStatus.OK, getFullInfoByHash(hashMD5));
        client.addForSend(message);
    }

    public void uploadPiece(String hashMD5, long id) throws IOException {
        Piece piece =
                ((PiecesService) AppContext.getInstance().get("piecesService"))
                .getPiece(hashMD5, id);
        Message message = new Message(MessageType.RESPONSE, MessageStatus.OK, piece);
        client.addForSend(message);
    }

    public void uploadCatalog(List<LocalFileInfoDTO> localFilesList) {
        Catalog catalog = new Catalog();

        for (LocalFileInfoDTO dto : localFilesList) {
            String hashMD5 = dto.getFileHash();
            String title = dto.getTitle();
            Long fileLength = dto.getFileSizeBytes();
            Integer pieceLength = PiecesService.PIECE_SIZE;

            catalog.put(hashMD5, new Info(hashMD5, pieceLength, fileLength, title));
        }
        Message message = new Message(MessageType.RESPONSE, MessageStatus.OK, catalog);
        client.addForSend(message);
    }

    public void uploadCatalog() {
        Catalog catalog = new Catalog();
        ObservableList<LocalFileInfoDTO> localFilesList =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                .getLocalFilesList();

        for (LocalFileInfoDTO dto : localFilesList) {
            String hashMD5 = dto.getFileHash();
            Info fullInfoByHash = getFullInfoByHash(hashMD5);
            catalog.put(hashMD5, fullInfoByHash);
        }

        Message message = new Message(MessageType.RESPONSE, MessageStatus.OK, catalog);
        client.addForSend(message);
    }

    public Info getFullInfoByHash(String hashMD5) {
        LocalFileInfoDTO localFileInfoDTO =
                ((LocalFileService) AppContext.getInstance().get("localFileService"))
                .getLocalFilesMap()
                .get(hashMD5);

        String title = localFileInfoDTO.getTitle();
        Long fileLength = localFileInfoDTO.getFileSizeBytes();
        Integer pieceLength = PiecesService.PIECE_SIZE;

        return new Info(hashMD5, pieceLength, fileLength, title);
    }

}
