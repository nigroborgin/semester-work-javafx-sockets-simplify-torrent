package ru.kpfu.itis.shkalin.simplifytorrent.service;

import org.apache.commons.codec.digest.DigestUtils;

import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.dto.LocalFileInfoDTO;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;

import java.io.*;

public class PiecesService {

    // TODO: delete hardcode
    public static final Integer PIECE_SIZE = 16384; // 16 Kb
    public static final String PATH_TO_DOWNLOAD_FILE = "C:\\Users\\Vyash\\Desktop";

    public Piece getPiece(String fullFileHashMD5, Long idPiece) throws IOException {
        LocalFileInfoDTO fileInfo = ((LocalFileService) AppContext.getInstance().get("localFileService"))
                .getLocalFilesMap()
                .get(fullFileHashMD5);
        FileInputStream inputStream = new FileInputStream(fileInfo.getFileLocalPath());
        inputStream.skip(idPiece * PIECE_SIZE);
        byte[] bytesOfPiece = inputStream.readNBytes(PIECE_SIZE);
        inputStream.close();
        return new Piece(fullFileHashMD5, idPiece, DigestUtils.sha1Hex(bytesOfPiece), bytesOfPiece);
    }

    public void putPieceInFile(Piece piece, RandomAccessFile file) throws IOException {
        file.seek(piece.getId() * PIECE_SIZE);
        file.write(piece.getBlockData());
    }

    public RandomAccessFile createFile(Info info) throws IOException {
        RandomAccessFile file = new RandomAccessFile(new File(PATH_TO_DOWNLOAD_FILE + File.separator + info.getTitle()), "rw");
        file.setLength(info.getFileLength());
        return file;
    }
}
