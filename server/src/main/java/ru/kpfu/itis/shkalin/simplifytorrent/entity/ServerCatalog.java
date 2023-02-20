package ru.kpfu.itis.shkalin.simplifytorrent.entity;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientHandler;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Connection;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCatalog implements Serializable {
    private Map<String, List<Connection>> hashToConnectionMap;
    private Map<String, Info> hashToInfoMap;

    public ServerCatalog() {
        this.hashToConnectionMap = new HashMap<>();
        this.hashToInfoMap = new HashMap<>();
    }

    public Map<String, Info> getHashToInfoMap() {
        return hashToInfoMap;
    }

    public Info getInfo(String hashMD5) {
        return hashToInfoMap.get(hashMD5);
    }

    public Connection getRandomConnection(String hashMD5) {
        List<Connection> clientHandlerList = hashToConnectionMap.get(hashMD5);
        int max = clientHandlerList.size() - 1;
        int min = 0;
        int randomIndex = (int) ((Math.random() * (max - min)) + min);
        return clientHandlerList.get(randomIndex);
    }

    public List<Connection> getConnectionList(String hashMD5) {
        return hashToConnectionMap.get(hashMD5);
    }

    public void put(Connection connection) {
        Catalog connectionLocalCatalog = connection.getLocalCatalog();
        if (connectionLocalCatalog == null) {
            connectionLocalCatalog = new Catalog();
        }
        hashToInfoMap.putAll(connectionLocalCatalog);

        Object[] objects = connectionLocalCatalog.keySet().toArray();
        for (Object key : objects) {
            if (hashToConnectionMap.containsKey(key.toString())) {
                List<Connection> connectionByKey = hashToConnectionMap.get(key.toString());
                connectionByKey.add(connection);
            } else {
                List<Connection> newArrayConnection = new ArrayList<>();
                newArrayConnection.add(connection);
                hashToConnectionMap.put(key.toString(), newArrayConnection);
            }
        }
    }

    public void put(String hashMD5, Info info, Connection connection) {
        hashToInfoMap.put(hashMD5, info);
        if (hashToConnectionMap.containsKey(hashMD5)) {
            hashToConnectionMap.get(hashMD5).add(connection);
        } else {
            ArrayList<Connection> newArrayConnection = new ArrayList<>();
            newArrayConnection.add(connection);
            hashToConnectionMap.put(hashMD5, newArrayConnection);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Object[] keys = hashToInfoMap.keySet().toArray();

        sb.append("ServerCatalog{\n");
        for (int i = 0; i < keys.length; i++) {
            sb.append("\n");
            sb.append("key: " + keys[i].toString() + "\n");
            sb.append("value: " + hashToInfoMap.get(keys[i]).toString() + "\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
