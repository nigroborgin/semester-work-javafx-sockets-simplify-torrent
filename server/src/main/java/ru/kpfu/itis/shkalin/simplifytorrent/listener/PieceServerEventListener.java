package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientHandler;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Connection;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerEventListenerException;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.MultiThreadServer;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;

import java.io.IOException;

public class PieceServerEventListener extends ServerEventListener{

    @Override
    public void handle(Message message) throws ServerException, IOException, ClassNotFoundException {
        if (!this.isInit) {
            throw new ServerEventListenerException("Listener has not been initiated yet.");
        }
        System.out.println("Piece accepted: \n\t"
                + this.clientHandler.getConnection()
                + "\n\t" + message + '\n');

        Piece data = (Piece) message.getData();
        switch (message.getType()) {

            case REQUEST:
                String hashMD5 = data.getHashMD5();
                Connection randomConnection = MultiThreadServer.serverCatalog.getRandomConnection(hashMD5);


                Message acceptedMessage = null;
                for (ClientHandler clientHandler : MultiThreadServer.clientHandlerList) {

                    if (randomConnection.equals(clientHandler.getConnection())) {

                        clientHandler.sendMessage(message);
                        System.out.println("Piece send: \n\t"
                                + clientHandler.getConnection()
                                + "\n\t" + message + '\n');

                        acceptedMessage = clientHandler.acceptMessage();
                        System.out.println("Piece accepted: \n\t"
                                + clientHandler.getConnection()
                                + "\n\t" + acceptedMessage + '\n');
                    }
                }

                this.clientHandler.sendMessage(acceptedMessage);
                System.out.println("Piece send: \n\t"
                        + this.clientHandler.getConnection()
                        + "\n\t" + message + '\n');
                break;

            case RESPONSE:
                break;

        }

    }

    @Override
    public boolean checkDataType(Message message) {
        return message.getData() instanceof Piece;
    }
}
