package com.example.brainsterquiz;


import android.app.Application;

import io.socket.client.IO;
import io.socket.client.Socket;


import java.net.URISyntaxException;

public class ChatApplication extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.96.4:4001/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}