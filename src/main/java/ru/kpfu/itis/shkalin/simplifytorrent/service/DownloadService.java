package ru.kpfu.itis.shkalin.simplifytorrent.service;

import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.CheckMessageHelper;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Client;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.MessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DownloadService {
    Client client;

    public DownloadService(Client client) {
        this.client = client;
    }

    public void downloadFile(String hashMD5) throws ClientException, MessageException, IOException {
        Info fileInfo = downloadInfo(hashMD5);
        RandomAccessFile file =
                ((PiecesService) AppContext.getInstance().get("piecesService"))
                .createFile(fileInfo);
        downloading(fileInfo, file);
    }

    private void downloading(Info fileInfo, RandomAccessFile file) throws ClientException, MessageException, IOException {
        long countPieces = fileInfo.getFileLength() / fileInfo.getPieceLength();
        if (fileInfo.getFileLength() % fileInfo.getPieceLength() != 0) {
            countPieces++;
        }
        PiecesService piecesService = (PiecesService) AppContext.getInstance().get("piecesService");
        for (long idPiece = 0; idPiece < countPieces; idPiece++) {
            Piece piece = downloadPiece(fileInfo.getHashMD5(), idPiece);
            piecesService.putPieceInFile(piece, file);
        }
    }

    public Info downloadInfo(String hashMD5) throws ClientException, MessageException {

        Info fileInfo;
        Message downloadInfoMessage = new Message(MessageType.REQUEST, MessageStatus.OK, new Info(hashMD5));
        client.addForSend(downloadInfoMessage);
        Message fullFileInfoMessage = client.addForAccept();
        fileInfo = (Info)
                ((CheckMessageHelper) AppContext.getInstance().get("checkMessageHelper"))
                .checkMessageAndGetData(fullFileInfoMessage, MessageType.RESPONSE);

        return fileInfo;
    }

    public Piece downloadPiece(String hashMD5, long id) throws ClientException, MessageException {

        Piece piece;
        Message downloadPieceMessage = new Message(MessageType.REQUEST, MessageStatus.OK, new Piece(hashMD5, id));
        client.addForSend(downloadPieceMessage);
        Message pieceMessage = client.addForAccept();
        piece = (Piece)
                ((CheckMessageHelper) AppContext.getInstance().get("checkMessageHelper"))
                .checkMessageAndGetData(pieceMessage, MessageType.RESPONSE);

        return piece;
    }

    public Catalog downloadCatalog() throws ClientException, MessageException {

        Catalog catalog;

        Message downloadCatalogMessage = new Message(MessageType.REQUEST, MessageStatus.OK, new Catalog());
        client.addForSend(downloadCatalogMessage);
        Message catalogMessage = client.addForAccept();
        catalog = (Catalog)
                ((CheckMessageHelper) AppContext.getInstance().get("checkMessageHelper"))
                .checkMessageAndGetData(catalogMessage, MessageType.RESPONSE);

        return catalog;
    }
}
