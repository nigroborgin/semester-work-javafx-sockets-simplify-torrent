package ru.kpfu.itis.shkalin.simplifytorrent.protocol.message;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.CheckStatusMessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.MessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;

public class CheckMessageHelper {

    public Object checkMessageAndGetData(Message message, MessageType expectedType) throws MessageException, CheckStatusMessageException {
        if (checkStatus(message.getStatus())
                && expectedType == message.getType()) {
            return message.getData();
        }
        throw new MessageException("Got wrong message: \n"
                + "expected: " + expectedType + " and " + MessageStatus.OK + '\n'
                + "fact: " + message.getType() + " and " + message.getStatus());

    }

    public boolean checkStatus(MessageStatus factStatus) throws CheckStatusMessageException {
        if (factStatus == MessageStatus.OK) {
            return true;
        }
        throw new CheckStatusMessageException();
    }
}
