package com.example.brainsterquiz;

import android.app.Application;

import io.socket.client.Socket;


public class con  extends Application {
    private Socket socket;

    public Socket getSocket(){
        return socket;
    }

    public Socket setSocket(Socket socket1){
        socket = socket1;
        return socket1;
    }

}
