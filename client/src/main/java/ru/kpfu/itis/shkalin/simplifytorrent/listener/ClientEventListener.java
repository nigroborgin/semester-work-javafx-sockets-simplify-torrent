package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Client;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;

import java.io.IOException;

public abstract class ClientEventListener {

    protected boolean isInit;
    protected Client client;

    public void init() {
        this.isInit = true;
        this.client = (Client) AppContext.getInstance().get("client");
    }

    public abstract void handle(Message message) throws IOException, ClassCastException, ClientException;

    public abstract boolean checkDataType(Message message);
}
