package com.example.postureup;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;
import java.nio.charset.StandardCharsets;


public class HC05Communication {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice hc05;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private boolean connected;

    @SuppressLint("MissingPermission")
    public HC05Communication() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String hc05Address = "00:22:09:01:06:FA"; // replace with your HC-05's address
        hc05 = bluetoothAdapter.getRemoteDevice(hc05Address);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try{
            socket = hc05.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connected = false;
    }

    @SuppressLint("MissingPermission")
    public boolean connect() {
        try {
            socket.connect();
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
        return connected;
    }

    public void disconnect() {
        try {
            socket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAngleData(byte[] angleData) {
        if (connected) {
            try {
                outputStream.write(angleData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readData() {
        if(!connected){
            return "";
        }
        byte[] buffer = new byte[1024];
        int numBytes;
        try {
            numBytes = inputStream.read(buffer);
            String stringData = new String(buffer, 0, numBytes, StandardCharsets.UTF_8);
            System.out.print("incoming data: ");
            System.out.println(stringData);
            return stringData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public float retrieveAngle(){
        String data = readData();
        if (data != ""){
            try{
                float angle = Float.parseFloat(data);
                return angle;
            }
            catch(Exception e){
                System.out.println(e);
                System.out.println("angle is " + data);
            }

        }
        return 0;
    }

}

