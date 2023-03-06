package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Connection;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerEventListenerException;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.MultiThreadServer;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

public class CatalogServerEventListener extends ServerEventListener {

    @Override
    public void handle(Message message) throws ServerException {
        if (!this.isInit) {
            throw new ServerEventListenerException("Listener has not been initiated yet.");
        }
        System.out.println("Catalog accepted: \n\t"
                + this.clientHandler.getConnection()
                + "\n\t" + message + '\n');

        switch (message.getType()) {

            case REQUEST:
                Catalog catalog = new Catalog(MultiThreadServer.serverCatalog.getHashToInfoMap());
                Message answer = new Message(MessageType.RESPONSE, MessageStatus.OK, catalog);
                this.clientHandler.sendMessage(answer);
                System.out.println("Catalog send: \n\t"
                        + this.clientHandler.getConnection()
                        + "\n\t" + answer + '\n');

            case RESPONSE:
                putCatalogInConnectionAndServerCatalog(this.clientHandler.getConnection(), message);
                break;
        }
    }

    @Override
    public boolean checkDataType(Message message) {
        return message.getData() instanceof Catalog;
    }

    private void putCatalogInConnectionAndServerCatalog(Connection connection, Message message) {
        Catalog catalogByMessage = (Catalog) message.getData();
        Object[] keysToCatalog = catalogByMessage.keySet().toArray();

        for (Object key : keysToCatalog) {
            String hashMD5 = catalogByMessage.get(key.toString()).getHashMD5();
            Info info = catalogByMessage.get(key.toString());
            connection.getLocalCatalog().put(hashMD5, info);

            MultiThreadServer.serverCatalog.put(hashMD5, info, connection);
        }
    }
}
