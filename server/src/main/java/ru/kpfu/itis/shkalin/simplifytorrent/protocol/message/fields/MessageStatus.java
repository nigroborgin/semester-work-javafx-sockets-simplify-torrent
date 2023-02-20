package ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields;

public enum MessageStatus {
    OK,
    NOT_INFO_ERROR,
    NOT_PIECE_ERROR,
    NOT_SIDS_ERROR,
    SERVER_ERROR;
}
