package ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;

public class CheckStatusMessageException extends MessageException {

    private MessageStatus messageStatus;

    public CheckStatusMessageException(MessageStatus ms) {
        super();
        this.messageStatus = ms;
    }

    public CheckStatusMessageException(String message, MessageStatus ms) {
        super(message);
        this.messageStatus = ms;
    }

    public CheckStatusMessageException(String message, Throwable cause, MessageStatus ms) {
        super(message, cause);
        this.messageStatus = ms;
    }

    public CheckStatusMessageException(Throwable cause, MessageStatus ms) {
        super(cause);
        this.messageStatus = ms;
    }

    public CheckStatusMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, MessageStatus ms) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.messageStatus = ms;
    }

    public CheckStatusMessageException() {
        super();
    }

    public CheckStatusMessageException(String message) {
        super(message);
    }

    public CheckStatusMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckStatusMessageException(Throwable cause) {
        super(cause);
    }

    public CheckStatusMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }
}
