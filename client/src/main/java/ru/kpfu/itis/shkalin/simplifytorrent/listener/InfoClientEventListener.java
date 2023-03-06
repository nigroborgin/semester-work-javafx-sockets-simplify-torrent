package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import javafx.util.Pair;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientEventListenerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.CheckMessageHelper;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.exception.MessageException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.DoFuture;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.WaitReceiveMessageList;

import java.util.List;
import java.util.stream.Collectors;

public class InfoClientEventListener extends ClientEventListener {

    @Override
    public void handle(Message message) throws ClientEventListenerException {
        if (!this.isInit) {
            throw new ClientEventListenerException("Listener has not been initiated yet.");
        }

        Info data;
        try {
            data = (Info) ((CheckMessageHelper) AppContext.getInstance().get("checkMessageHelper"))
                    .checkMessageAndGetData(message, MessageType.RESPONSE);
        } catch (MessageException e) {
            throw new ClientEventListenerException(e);
        }
        String hashMD5 = data.getHashMD5();

        switch (message.getType()) {
            case REQUEST:
                Message answer;
                Info info =
                        ((LocalFileService) AppContext.getInstance().get("localFileService"))
                                .getLocalCatalog()
                                .get(hashMD5);
                if (info == null) {
                    answer = new Message(MessageType.RESPONSE, MessageStatus.NOT_INFO_ERROR, data);
                } else {
                    answer = new Message(MessageType.RESPONSE, MessageStatus.OK, info);
                }
                this.client.send(answer, null);
                break;

            case RESPONSE:
                WaitReceiveMessageList waitReceiveList = this.client.getWaitReceiveList();
                List<Pair<Message, DoFuture>> waiterMessages =
                        waitReceiveList.stream()
                                .filter((m) -> m.getKey().getData() instanceof Info)
                                .filter((m) -> ((Info) m.getKey().getData()).getHashMD5().equals(hashMD5))
                                .collect(Collectors.toList());
                Message key = waiterMessages.get(0).getKey();
                DoFuture value = waiterMessages.get(0).getValue();
                value.doNext(key.getData());

                waitReceiveList.removeAll(waiterMessages);
                break;
        }
    }

    @Override
    public boolean checkDataType(Message message) {
        return message.getData() instanceof Info;
    }

}
