package org.example.socket.server.util;

import org.apache.commons.lang3.StringUtils;
import org.example.socket.server.model.ConnectionWrapper;
import org.example.socket.server.model.OnlineUsers;
import org.example.socket.server.model.User;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Util {

    private static Map<String, Socket> clientsMap = new HashMap<>();

    public static void sendDateToClient10x(Socket clientSocket, ExecutorService workers) throws IOException {
        WriteData writeData = new
                WriteData(clientSocket.getOutputStream());
        workers.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    writeData.println(new Date().toString());
                    Thread.sleep(1000);
                }
                clientSocket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void handleClientLogin(Socket clientSocket, ExecutorService workers) {
        workers.execute(() -> {
            try {
                ReadData readData =
                        new ReadData(clientSocket.getInputStream());
                String loginCommand = readData.read();
                String[] loginCommands = StringUtils.split(loginCommand);
                if (loginCommands[0].equalsIgnoreCase("login")) {
                    System.out.println("User Logged in successfully!");
                    User user = createUser(loginCommands[1]);
                    clientsMap.put(user.getUsername(), clientSocket);
                    markUserAsOnline(user);
                    showOnlineUsers(workers);
                    WriteData writeData = new WriteData(clientSocket.getOutputStream());
                    writeData.println("You have successfully logged in! Start chat with online users");
                    String msg = readData.read();
                    String[] msgTokens = StringUtils.split(msg);
                    sendMessage(user.getUsername(), msgTokens[1], msgTokens[2], workers);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
    }

    private static void sendMessage(String from, String to, String msgBody, ExecutorService workers) {
        workers.execute(() -> {
            try {
                Socket clientSocket = clientsMap.get(to);
                WriteData writeData = new WriteData(clientSocket.getOutputStream());
                writeData.println(from + "  says:  " + msgBody);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public static void addNewConnection(Socket clientSocket) {
        ConnectionWrapper.getConnections().add(clientSocket);
    }

    private static class WriteData {

        private OutputStream dataStream;

        public WriteData(OutputStream dataStream) {
            this.dataStream = dataStream;
        }

        public void println(String data) throws IOException {
            PrintWriter writer = new PrintWriter(dataStream, true);
            writer.println(data + "\n");
        }
    }

    public static class ReadData {

        private InputStream dataStream;

        public ReadData(InputStream dataStream) {
            this.dataStream = dataStream;
        }

        public String read() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream));
            String s;
            s = reader.readLine();
            return s;
        }
    }

    private static User createUser(String username) {
        return new User(username);
    }

    private static void showOnlineUsers(ExecutorService workers) throws IOException {
        workers.execute(() -> ConnectionWrapper.getConnections()
                .forEach(clientSocket -> {
                    try {
                        WriteData writeData = new WriteData(clientSocket.getOutputStream());
                        for (User onlineUser : OnlineUsers.getOnlineUsers()) {
                            writeData.println(onlineUser.getUsername() + " online");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
    }

    private static void markUserAsOnline(User user) {
        OnlineUsers.getOnlineUsers().add(user);
    }
}
