package com.studienprojekt.smarthome.interfaces;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.studienprojekt.smarthome.activities.HttpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class EchoWebSocketListener extends WebSocketListener {

    HttpActivity activity;
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private MessageAdapter messageAdapter;


    public EchoWebSocketListener(HttpActivity activity) {
        this.activity = activity;
    }




    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Verbindung erfolgreich hergestellt", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMessage(WebSocket webSocket, final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("message", text);
                    jsonObject.put("byServer", true);

                    messageAdapter.addItem(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //is called just before connection is closed
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    //is called when connection closes
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    //is called when connection fail
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
    }
}
