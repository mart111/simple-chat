package org.example.socket.server.model;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsers {

    private static List<User> onlineUsers;

    public static List<User> getOnlineUsers() {
        if (onlineUsers == null)
            onlineUsers = new ArrayList<>();

        return onlineUsers;
    }
}
