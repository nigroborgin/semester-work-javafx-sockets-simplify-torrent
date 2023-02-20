package ru.kpfu.itis.shkalin.simplifytorrent.protocol.message;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {

    private MessageType type;
    private MessageStatus status;
    private Object data = null;

    public Message(MessageType type, MessageStatus status, Object data) {
        this.type = type;
        this.status = status;
        this.data = data;
    }

    public Message(MessageType type, MessageStatus status) {
        this.type = type;
        this.status = status;
    }

    public MessageType getType() {
        return type;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return type == message.type && status == message.status && Objects.equals(data, message.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status, data);
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", status=" + status +
                ", data=" + data.toString() +
                '}';
    }
}
