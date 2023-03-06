package ru.kpfu.itis.shkalin.simplifytorrent.structure;

import ru.kpfu.itis.shkalin.simplifytorrent.entity.Info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Catalog extends HashMap<String, Info> implements Serializable {

    public Catalog(Map<String, Info> map) {
        this.putAll(map);
    }

    public Catalog() {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Object[] keys = super.keySet().toArray();
        sb.append("Catalog{\n");

        for (int i = 0; i < keys.length; i++) {
            sb.append("\n");
            sb.append("key: " + keys[i].toString() + "\n");
            sb.append("value: " + super.get(keys[i]).toString() + "\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
