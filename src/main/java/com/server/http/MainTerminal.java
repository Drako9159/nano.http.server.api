package com.server.http;

public class MainTerminal {
    public static void main(String[] args) {

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                ServerRun server = new ServerRun();
                server.start();
            }
        };
        thread.run();


    }
}
