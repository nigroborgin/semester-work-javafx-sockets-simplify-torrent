package ru.kpfu.itis.shkalin.simplifytorrent.structure;

import javafx.util.Pair;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class WaitReceiveMessageList extends ArrayList<Pair<Message, DoFuture>> {

    @Override
    public boolean contains(Object o) {
        return getMessageList().contains(o);
    }

    public List<Message> getMessageList() {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            messages.add(get(i).getKey());
        }
        return messages;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < c.size(); j++) {

            Message nextMessage = ((Pair<Message, DoFuture>) c.iterator().next()).getKey();
                if (Objects.equals(get(i).getKey(), nextMessage)) {
                    remove(this.get(i));
                }
            }

        }

        return true;
    }
}
