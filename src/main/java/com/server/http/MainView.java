package com.server.http;

import com.server.http.view.Welcome;

import java.awt.*;

public class MainView {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    Welcome frame = new Welcome();
                    frame.setVisible(true);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
