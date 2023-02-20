package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientHandler;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;

import java.io.IOException;

public abstract class ServerEventListener {

    protected boolean isInit;
    protected ClientHandler clientHandler;

    public void init(ClientHandler clientHandler) {
        this.isInit = true;
        this.clientHandler = clientHandler;
    }

    public abstract void handle(Message message) throws ServerException, IOException, ClassNotFoundException;

    public abstract boolean checkDataType(Message message);
}
