package org.example.socket.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConnectionWrapper {

    private static List<Socket> clientSockets;

    private ConnectionWrapper() {

    }

    public static List<Socket> getConnections() {
        if (clientSockets == null) {
            clientSockets = new ArrayList<>();
        }

        return clientSockets;
    }
}
