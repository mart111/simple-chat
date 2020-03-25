package org.example.socket.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);


        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    Socket socket = new Socket(InetAddress.getLocalHost(), 9088);
                    ReadData readData = new ReadData(socket.getInputStream());
                    System.out.println(readData.readData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static class ReadData {

        private InputStream dataStream;

        public ReadData(InputStream dataStream) {
            this.dataStream = dataStream;
        }

        public String readData() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream));
            String s;
            StringBuffer buffer = new StringBuffer();
            while ((s = reader.readLine()) != null) {
                buffer.append(s);
                buffer.append("\n");
            }

            return buffer.toString();
        }
    }

    static class WriteData {

        private OutputStream dataStream;

        public WriteData(OutputStream dataStream) {
            this.dataStream = dataStream;
        }

        public void sendData(String data) throws IOException {
            PrintWriter writer = new PrintWriter(dataStream, true);
            writer.println(data);
        }
    }

}
