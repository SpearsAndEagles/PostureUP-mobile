package com.example.postureup;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private HC05Communication hc05Communication;
    private boolean connected = false;

    private Button connectButton;
    private Button disconnectButton;
    private TextView statusText;
    private TextView angleText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("UPosture");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        hc05Communication = new HC05Communication();

        connectButton = (Button) findViewById(R.id.connect_button);
        statusText = (TextView) findViewById(R.id.status_text);
        angleText = (TextView) findViewById(R.id.angle);


        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connected) {
                    if (hc05Communication.connect()) {
                        connected = true;
                        statusText.setText("Connected");
                    }
                }
            }
        });

        AngleView angleView = findViewById(R.id.angleView);
        // Set the initial angle to 0
        angleView.setAngle(0);

        // Update the angle every second
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            // Calculate the angle based on your angle data
            @Override
            public void run() {
                float angle = hc05Communication.retrieveAngle();

                float finalAngle = angle;
                runOnUiThread(() -> {
                    // Update the angle view
                    angleView.setAngle(finalAngle);
                    angleText.setText("angle: " + finalAngle);
                });
            }

        },0, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connected) {
            hc05Communication.disconnect();
        }
    }
}
