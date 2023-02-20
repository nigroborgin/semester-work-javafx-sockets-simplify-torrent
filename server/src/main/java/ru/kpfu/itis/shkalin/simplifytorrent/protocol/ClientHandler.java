package ru.kpfu.itis.shkalin.simplifytorrent.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ru.kpfu.itis.shkalin.simplifytorrent.exception.ServerException;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.CatalogServerEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.InfoServerEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.PieceServerEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.ServerEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.MessageManager;

public class ClientHandler extends Thread {

    protected List<ServerEventListener> listeners;
    protected Connection connection;
    protected boolean started;
    protected MessageManager messageManager;
    private OutputStream outputStream;
    private InputStream inputStream;

    public ClientHandler(Connection connection) throws IOException {
        this.listeners = new ArrayList<>();
        this.connection = connection;
        this.started = false;
        this.messageManager = new MessageManager();
    }

    @Override
    public void run() {
        started = true;
        this.registerListener(new CatalogServerEventListener());
        this.registerListener(new InfoServerEventListener());
        this.registerListener(new PieceServerEventListener());

        try {
            outputStream = connection.getOutputStream();
            inputStream = connection.getInputStream();

            while (!connection.getSocket().isClosed()) {
                acceptMessage();
            }

            // Closing this socket will also close the socket's InputStream and OutputStream.
            connection.getSocket().close();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerListener(ServerEventListener listener) {
        listener.init(this);
        this.listeners.add(listener);
    }

    public Message acceptMessage() throws IOException, ClassNotFoundException, ServerException {
        Message message = messageManager.readMessage(inputStream);
        for (ServerEventListener listener : listeners) {
            if (listener.checkDataType(message)) {
                listener.handle(message);
            }
        }
        return message;
    }

    public void sendMessage(Message message) throws ServerException {
        if (!started) {
            throw new ServerException("Server hasn't been started yet.");
        }
        try {

            byte[] bytesMessage = messageManager.getBytes(message);
            int messageLength = bytesMessage.length;

            ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
            b.putInt(messageLength);
            byte[] messageLengthBytes = b.array();

            byte[] bytesForSend = new byte[messageLength + Integer.BYTES];

            for (int i = 0; i < bytesForSend.length; i++) {
                if (i < Integer.BYTES) {
                    bytesForSend[i] = messageLengthBytes[i];
                } else {
                    bytesForSend[i] = bytesMessage[i - Integer.BYTES];
                }
            }
            outputStream.write(bytesForSend);
            outputStream.flush();

        } catch (IOException e) {
            throw new ServerException("Can't send message.", e);
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Connection getConnection() {
        return connection;
    }
}