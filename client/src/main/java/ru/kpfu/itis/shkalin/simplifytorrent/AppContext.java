package ru.kpfu.itis.shkalin.simplifytorrent;

import ru.kpfu.itis.shkalin.simplifytorrent.protocol.Client;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.ClientException;
import ru.kpfu.itis.shkalin.simplifytorrent.protocol.message.CheckMessageHelper;
import ru.kpfu.itis.shkalin.simplifytorrent.service.DownloadService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.LocalFileService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.PiecesService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.UploadService;
import ru.kpfu.itis.shkalin.simplifytorrent.service.converter.ConverterServiceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class AppContext extends HashMap<String, Object> {

    private static volatile AppContext instance;

    private AppContext(){

    }

    public static AppContext getInstance() {
        AppContext localInstance = instance;
        if (localInstance == null) {
            synchronized (AppContext.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AppContext();
                }
            }
        }
        return localInstance;
    }

    public void init() throws UnknownHostException, ClientException {

        // TODO: delete hardcode
        Client client = new Client(InetAddress.getByName("127.0.0.1"), 1234);
        client.startClient();

        instance.put("client", client);

        CheckMessageHelper checker = new CheckMessageHelper();
        instance.put("checkMessageHelper", checker);

        PiecesService piecesService = new PiecesService();
        instance.put("piecesService", piecesService);
        instance.put("converterService", new ConverterServiceImpl());
        LocalFileService localFileService = new LocalFileService();
        instance.put("localFileService", localFileService);
        instance.put("downloadService", new DownloadService(client));
        instance.put("uploadService", new UploadService(client));
    }

    @Override
    public String toString() {
        Object[] keysArray = instance.keySet().toArray();
        StringBuilder returnString = new StringBuilder();
        returnString.append("AppContext{\n");
        for (Object key : keysArray) {
            returnString.append('\t').append(key.toString());
            returnString.append(" = ");
            returnString.append(instance.get(key.toString()));
            returnString.append('\n');
        }
        returnString.append('}');
        return returnString.toString();
    }
}
