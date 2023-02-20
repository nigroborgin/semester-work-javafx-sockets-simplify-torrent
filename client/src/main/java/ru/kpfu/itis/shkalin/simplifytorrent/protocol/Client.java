package ru.kpfu.itis.shkalin.simplifytorrent.protocol;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.MessageManager;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

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
    private ClientAccepter clientAccepter;
    private Queue<Message> waitSendQueue;
    private List<Message> waitAcceptList;

    public Client(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.waitSendQueue = new ArrayDeque<>();
        this.waitAcceptList = new ArrayList<>();
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
        clientAccepter = new ClientAccepter();
        clientSender.start();
        clientAccepter.start();
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

    private class ClientAccepter extends Thread {
        private MessageManager messageManager;

        @Override
        public void run() {
            this.messageManager = new MessageManager();

            while (!socket.isClosed()) {
                if (!waitSendQueue.isEmpty()) {
                    try {
                        Message message = acceptMessage();
                        // TODO: for(listeners)...
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public Message acceptMessage() throws ClientException {
            try {
                Message message = messageManager.readMessage(inputStream);
                System.out.println("acceptMessage: \n" + message);
                return message;
            } catch (IOException | ClassNotFoundException e) {
                throw new ClientException("Error when accepted the message", e);
            }
        }

    }

    public void addForSend(Message message) {
        // TODO: сделать if REQ --> добавление сообщения в структуру ждунов приёма сообщений
        if (message.getType() == MessageType.REQUEST) {
            // TODO: сделать listeners и проверку по всем ним
            waitAcceptList.add(new Message(MessageType.RESPONSE, message.getStatus(), message.getData()));
        }
        waitSendQueue.add(message);
    }
}