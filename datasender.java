package io.github.controlwear.joystickdemo;

import android.os.AsyncTask;
import java.io.DataOutputStream;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class datasender extends AsyncTask<String,Void,Void> {

    Socket s;
    PrintWriter pw;

    String IP;
    String port;
    String message;


    @Override
    protected Void doInBackground(String... voids) {
        message = voids[0];
        IP = voids[1];
        port = voids[2];

        try {
            s = new Socket(IP, Integer.parseInt(port));
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}