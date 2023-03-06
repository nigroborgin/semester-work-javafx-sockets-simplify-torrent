package ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception;

public class ClientEventListenerException extends ClientException {

    public ClientEventListenerException() {
    }

    public ClientEventListenerException(String message) {
        super(message);
    }

    public ClientEventListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientEventListenerException(Throwable cause) {
        super(cause);
    }

    public ClientEventListenerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
