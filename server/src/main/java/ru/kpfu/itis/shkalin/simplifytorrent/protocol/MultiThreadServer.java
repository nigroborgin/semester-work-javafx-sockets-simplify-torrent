package ru.kpfu.itis.shkalin.simplifytorrent.protocol;


import ru.kpfu.itis.shkalin.simplifytorrent.structure.ServerCatalog;
import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {

    private final ExecutorService executeIt;
    private final Integer port;
    public static ServerCatalog serverCatalog;
    public static List<ClientHandler> clientHandlerList;
    private boolean started;


    public MultiThreadServer(int countThreads, int port) {
        this.executeIt = Executors.newFixedThreadPool(countThreads);
        this.port = port;
        clientHandlerList = new ArrayList<>();
        serverCatalog = new ServerCatalog();
        this.started = false;
    }

    public void startServer() {
        started = true;

        try (ServerSocket server = new ServerSocket(port)) {

            while (!server.isClosed()) {
                Connection client = new Connection(server.accept());
                ClientHandler clientHandler = new ClientHandler(client);
                executeIt.execute(clientHandler);
                clientHandlerList.add(clientHandler);
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadCastMessage(Message message) throws ServerException {
        if (!started) {
            throw new ServerException("Server hasn't been started yet.");
        }
        for (ClientHandler clientHandler : MultiThreadServer.clientHandlerList) {
            clientHandler.sendMessage(message);
        }
    }

    public void sendMessageByConnection(Connection connection, Message message) throws ServerException {
        if (!started) {
            throw new ServerException("Server hasn't been started yet.");
        }
        for (ClientHandler clientHandler : MultiThreadServer.clientHandlerList) {
            if (connection.equals(clientHandler.getConnection())) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public Message acceptMessageByConnection(Connection connection) throws ServerException, IOException, ClassNotFoundException {
        if (!started) {
            throw new ServerException("Server hasn't been started yet.");
        }
        Message message = null;

        return message;
    }
}