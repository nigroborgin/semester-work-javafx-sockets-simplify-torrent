package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.ServerCatalog;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerEventListenerException;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.MultiThreadServer;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

public class InfoServerEventListener extends ServerEventListener {

    @Override
    public void handle(Message message) throws ServerException {
        if (!this.isInit) {
            throw new ServerEventListenerException("Listener has not been initiated yet.");
        }
        Message answer;
        Info data = (Info) message.getData();
        String hashMD5 = data.getHashMD5();
        System.out.println("Info accepted: \n\t"
                + this.clientHandler.getConnection()
                + "\n\t" + message + '\n');

        switch (message.getType()) {

            case REQUEST:
                Info info = MultiThreadServer.serverCatalog.getInfo(hashMD5);

                if (info == null) {
                    answer = new Message(MessageType.RESPONSE, MessageStatus.NOT_INFO_ERROR, data);
                } else {
                    answer = new Message(MessageType.RESPONSE, MessageStatus.OK, info);
                }

                this.clientHandler.sendMessage(answer);
                System.out.println("Info send: \n\t"
                        + this.clientHandler.getConnection()
                        + "\n\t" + answer + '\n');
                break;

            case RESPONSE:
                MultiThreadServer.serverCatalog
                        .getHashToInfoMap()
                        .put(data.getHashMD5(), data);
                ServerCatalog serverCatalog = MultiThreadServer.serverCatalog;
                serverCatalog.put(hashMD5, data, this.clientHandler.getConnection());
                break;
        }

    }

    @Override
    public boolean checkDataType(Message message) {
        return message.getData() instanceof Info;
    }

}
