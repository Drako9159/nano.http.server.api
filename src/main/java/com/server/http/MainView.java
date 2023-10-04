package com.server.http;

import com.server.http.view.ViewUI;

import java.awt.*;

public class MainView {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    ViewUI frame = new ViewUI();
                    frame.setVisible(true);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
