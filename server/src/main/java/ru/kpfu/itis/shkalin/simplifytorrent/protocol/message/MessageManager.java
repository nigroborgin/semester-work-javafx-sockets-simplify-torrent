package ru.kpfu.itis.shkalin.simplifytorrent.protocol.message;

import java.io.*;
import java.nio.ByteBuffer;

public class MessageManager {

    public byte[] getBytes(Message message) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(message);
            return bos.toByteArray();
        }
    }

    public Message readMessage(InputStream in) throws ClassNotFoundException, IOException {
        Message message = null;
        ByteBuffer b = ByteBuffer.allocate(Integer.BYTES);
        byte[] messageLengthBytes = b.array();

        while (!(in.available() > 0)) {

        }

        in.read(messageLengthBytes, 0, Integer.BYTES);
        int messageLength = ByteBuffer.wrap(messageLengthBytes).getInt();
        byte[] bytesMessage = new byte[messageLength];
        in.read(bytesMessage, 0, messageLength);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytesMessage);
             ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream)) {
            message = (Message) ois.readObject();
        } catch (EOFException ignored) {
        }

        return message;
    }
}
