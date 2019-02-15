package io.github.controlwear.joystickdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {


    private TextView mTextViewAngleLeft;
    private TextView mTextViewStrengthLeft;

    private TextView mTextViewAngleRight;
    private TextView mTextViewStrengthRight;
    private TextView mTextViewCoordinateRight;
    private EditText editText1;
    private int JoystickAngle;
    private int Strength;
    Socket s;
    String IP = "192.168.1.16"; //IP address of the Server
    String port = "4444";       //Port of the server

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //editText1 = findViewById(R.id.editText3);
        //IP = (editText1.getText())+"";

        mTextViewAngleLeft = (TextView) findViewById(R.id.textView_angle_left);
        mTextViewStrengthLeft = (TextView) findViewById(R.id.textView_strength_left);

        JoystickView joystickLeft = (JoystickView) findViewById(R.id.joystickView_left);
        joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mTextViewAngleLeft.setText(angle + "°");
                mTextViewStrengthLeft.setText(strength + "%");
            }
        });


        mTextViewAngleRight = (TextView) findViewById(R.id.textView_angle_right);
        mTextViewStrengthRight = (TextView) findViewById(R.id.textView_strength_right);
        mTextViewCoordinateRight = findViewById(R.id.textView_coordinate_right);

        final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength) {
                if(angle > 180)
                    angle = -1 * (angle - 360);
                mTextViewAngleRight.setText(angle + "°");
                mTextViewStrengthRight.setText(strength + "%");
                Strength = strength;
                mTextViewCoordinateRight.setText(
                        String.format("x%03d:y%03d",
                                joystickRight.getNormalizedX(),
                                joystickRight.getNormalizedY())
                );

                JoystickAngle = (angle);
                sendData();
            }
        });
    }
    public void sendData(){
        String data = String.format("%03d,%03d",
                        Strength,
                        JoystickAngle);
        editText1 = findViewById(R.id.editText3);
        IP = editText1.getText().toString();
        datasender dataSender = new datasender();
        dataSender.execute(data, IP, port);
    }
}
