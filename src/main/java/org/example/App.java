package org.example;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws InterruptedException, IOException, KeyManagementException, NoSuchAlgorithmException {

        new App().serve();
    }


//    @LogExecutionTime
    public int serve() throws InterruptedException {
        System.out.println("Serve()");

        return 100;
    }
}
