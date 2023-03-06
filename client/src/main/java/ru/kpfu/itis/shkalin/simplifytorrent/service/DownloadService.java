package ru.kpfu.itis.shkalin.simplifytorrent.service;

import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Client;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.MessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.DoFuture;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DownloadService {
    Client client;

    public DownloadService(Client client) {
        this.client = client;
    }

    public void downloadFile(String hashMD5) throws ClientException, MessageException, IOException {

        DoFuture<Info> infoWillDoFuture =
                (Info info) -> {
                    RandomAccessFile file = null;
                    try {
                        file = ((PiecesService) AppContext.getInstance().get("piecesService"))
                                .createFile(info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    long countPieces = info.getFileLength() / info.getPieceLength();
                    if (info.getFileLength() % info.getPieceLength() != 0) {
                        countPieces++;
                    }

                    for (long idPiece = 0; idPiece < countPieces; idPiece++) {
                        RandomAccessFile finalFile = file;
                        DoFuture<Piece> pieceWillDoFuture =
                                (Piece piece) -> {
                                    try {
                                        assert finalFile != null;
                                        ((PiecesService) AppContext.getInstance().get("piecesService"))
                                                .putPieceInFile(piece, finalFile);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                };
                        downloadPiece(info.getHashMD5(), idPiece, pieceWillDoFuture);
                    }
                };
        downloadInfo(hashMD5, infoWillDoFuture);
    }

    public void downloadInfo(String hashMD5, DoFuture<Info> willDoFuture) {

        Message downloadInfoMessage = new Message(MessageType.REQUEST, MessageStatus.OK, new Info(hashMD5));
        client.send(downloadInfoMessage, willDoFuture);

    }

    public void downloadPiece(String hashMD5, long id, DoFuture<Piece> willDoFuture) {

        Message downloadPieceMessage = new Message(MessageType.REQUEST, MessageStatus.OK, new Piece(hashMD5, id));
        client.send(downloadPieceMessage, willDoFuture);

    }

    public void downloadCatalog() {

        Message downloadCatalogMessage = new Message(MessageType.REQUEST, MessageStatus.OK, new Catalog());

        DoFuture<Catalog> catalogWillDoFuture =
                (Catalog catalog) -> ((LocalFileService) AppContext.getInstance().get("localFileService"))
                        .updateServerCatalog(catalog);

        client.send(downloadCatalogMessage, catalogWillDoFuture);

    }


}
