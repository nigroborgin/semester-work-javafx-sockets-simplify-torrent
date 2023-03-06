package ru.kpfu.itis.shkalin.simplifytorrent.protocol;

import javafx.util.Pair;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.CatalogClientEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.ClientEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.InfoClientEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.listener.PieceClientEventListener;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.MessageManager;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.DoFuture;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.WaitReceiveMessageList;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Client {

    private final InetAddress address;
    private final int port;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ClientSender clientSender;
    private ClientReceiver clientReceiver;
    private Queue<Message> waitSendQueue;
    private WaitReceiveMessageList waitReceiveList;


    public Client(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.waitSendQueue = new ArrayDeque<>();
        this.waitReceiveList = new WaitReceiveMessageList();
    }

    public void startClient() throws ClientException {

        try {
            socket = new Socket(address, port);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new ClientException("Can't connect.", e);
        }

        clientSender = new ClientSender();
        clientSender.setName("SENDER-THREAD");
        clientReceiver = new ClientReceiver();
        clientReceiver.setName("RECEIVER-THREAD");
        clientSender.start();
        clientReceiver.start();
        return;
    }

    private class ClientSender extends Thread {

        private MessageManager messageManager;

        @Override
        public void run() {
            this.messageManager = new MessageManager();

            while (!socket.isClosed()) {
                if (!waitSendQueue.isEmpty()) {
                    try {
                        sendMessage(waitSendQueue.poll());
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void sendMessage(Message message) throws ClientException {
            try {
                messageManager.writeMessage(message, outputStream);
                System.out.println("sendMessage: \n" + message);
            } catch (IOException e) {
                throw new ClientException("Error when sending a message", e);
            }
        }

    }

    private class ClientReceiver extends Thread {
        private List<ClientEventListener> listeners;
        private MessageManager messageManager;

        @Override
        public void run() {
            listeners = new ArrayList<>();
            this.registerListener(new CatalogClientEventListener());
            this.registerListener(new InfoClientEventListener());
            this.registerListener(new PieceClientEventListener());
            this.messageManager = new MessageManager();

            while (!socket.isClosed()) {
                if (!waitSendQueue.isEmpty()) {
                    try {
                        Message message = receiveMessage();
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handle(message);
                        }
                    } catch (ClassCastException ignored) {

                    } catch (ClientException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Message receiveMessage() throws ClientException {
            try {
                Message message = messageManager.readMessage(inputStream);
                System.out.println("receiveMessage: \n" + message);
                return message;
            } catch (IOException | ClassNotFoundException e) {
                throw new ClientException("Error when receive the message", e);
            }
        }

        public void registerListener(ClientEventListener listener) {
            listener.init();
            this.listeners.add(listener);
        }
    }

    public void send(Message message, DoFuture<?> willDoFuture) {
        boolean itWillSend = true;

        if (message.getType() == MessageType.REQUEST) {
            Message waiterMessage = new Message(MessageType.RESPONSE, message.getStatus(), message.getData());
            if (!waitReceiveList.contains(waiterMessage)) {
                waitReceiveList.add(new Pair<>(waiterMessage, willDoFuture));
            } else {
                itWillSend = false;
            }
        }
        if (!waitSendQueue.contains(message) && itWillSend) {
            waitSendQueue.add(message);
        }
    }

    public Queue<Message> getWaitSendQueue() {
        return waitSendQueue;
    }

    public WaitReceiveMessageList getWaitReceiveList() {
        return waitReceiveList;
    }

}