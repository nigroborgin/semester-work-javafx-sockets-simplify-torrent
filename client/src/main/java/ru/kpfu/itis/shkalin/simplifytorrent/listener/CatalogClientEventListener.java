package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import javafx.util.Pair;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.Catalog;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientEventListenerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.DoFuture;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.WaitReceiveMessageList;

import java.util.List;
import java.util.stream.Collectors;

public class CatalogClientEventListener extends ClientEventListener {

    @Override
    public void handle(Message message) throws ClientException {
        if (!this.isInit) {
            throw new ClientEventListenerException("Listener has not been initiated yet.");
        }
        switch (message.getType()) {

            case REQUEST:
                Catalog catalog =
                        ((LocalFileService) AppContext.getInstance().get("localFileService"))
                                .getLocalCatalog();
                Message answer = new Message(MessageType.RESPONSE, MessageStatus.OK, catalog);
                this.client.send(answer, null);
                break;

            case RESPONSE:
                WaitReceiveMessageList waitReceiveList = this.client.getWaitReceiveList();
                List<Pair<Message, DoFuture>> waiterMessages =
                        waitReceiveList.stream()
                                .filter((m) -> m.getKey().getData() instanceof Catalog)
                                .collect(Collectors.toList());

                Message key = waiterMessages.get(0).getKey();
                DoFuture value = waiterMessages.get(0).getValue();
                value.doNext(message.getData());

                waitReceiveList.removeAll(waiterMessages);
                break;
        }
    }

    @Override
    public boolean checkDataType(Message message) {
        return message.getData() instanceof Catalog;
    }

}
