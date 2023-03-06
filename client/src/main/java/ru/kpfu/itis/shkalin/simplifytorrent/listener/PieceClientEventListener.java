package ru.kpfu.itis.shkalin.simplifytorrent.listener;

import javafx.util.Pair;
import ru.kpfu.itis.shkalin.simplifytorrent.AppContext;
import ru.kpfu.itis.shkalin.simplifytorrent.entity.Piece;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientEventListenerException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.exception.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageStatus;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.fields.MessageType;
import ru.kpfu.itis.shkalin.simplifytorrent.service.PiecesService;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.DoFuture;
import ru.kpfu.itis.shkalin.simplifytorrent.structure.WaitReceiveMessageList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PieceClientEventListener extends ClientEventListener {

    @Override
    public void handle(Message message) throws ClientException, IOException, ClassCastException {
        if (!this.isInit) {
            throw new ClientEventListenerException("Listener has not been initiated yet.");
        }

        Piece data = (Piece) message.getData();
        switch (message.getType()) {

            case REQUEST:
                Message answer;
                Piece piece = ((PiecesService) AppContext.getInstance().get("piecesService"))
                        .getPiece(data.getHashMD5(), data.getId());
                if (piece == null) {
                    answer = new Message(MessageType.RESPONSE, MessageStatus.NOT_INFO_ERROR, data);
                } else {
                    answer = new Message(MessageType.RESPONSE, MessageStatus.OK, piece);
                }
                this.client.send(answer, null);
                break;

            case RESPONSE:
                WaitReceiveMessageList waitReceiveList = this.client.getWaitReceiveList();
                List<Pair<Message, DoFuture>> waiterMessages =
                        waitReceiveList.stream()
                                .filter((m) -> m.getKey().getData() instanceof Piece)
                                .filter((m) -> m.getKey().getData().equals(data))
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
        return message.getData() instanceof Piece;
    }
}
