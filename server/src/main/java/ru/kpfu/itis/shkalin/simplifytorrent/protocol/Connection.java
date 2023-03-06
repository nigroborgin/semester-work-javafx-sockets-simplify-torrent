package ru.kpfu.itis.shkalin.simplifytorrent.protocol;

import ru.kpfu.itis.shkalin.simplifytorrent.structure.Catalog;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Connection {

    private Socket socket;
    private Catalog localCatalog;

    public Connection(Socket socket, Catalog localCatalog) {
        this.socket = socket;
        this.localCatalog = localCatalog;
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.localCatalog = new Catalog();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Catalog getLocalCatalog() {
        return localCatalog;
    }

    public void setLocalCatalog(Catalog localCatalog) {
        this.localCatalog = localCatalog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Connection)) return false;
        Connection that = (Connection) o;
        return Objects.equals(socket, that.socket) && Objects.equals(localCatalog, that.localCatalog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, localCatalog);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "\n\t\tinetAddress= " + socket.getInetAddress() +
                "\n\t\tlocalAddress= " + socket.getLocalAddress() +
                "\n\t\tinetPort= " + socket.getPort() +
                "\n\t\tlocalPort= " + socket.getLocalPort() +
                "\n\t\tlocalCatalog= " + localCatalog +
                "}";
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
