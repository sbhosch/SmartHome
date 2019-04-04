package com.studienprojekt.smarthome.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.studienprojekt.smarthome.R;
import com.studienprojekt.smarthome.interfaces.EchoWebSocketListener;
import com.studienprojekt.smarthome.interfaces.MessageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class HttpActivity extends AppCompatActivity {

    private Button bStartConnection, bTestSwitch;
    private TextView send;
    private TextView messageBox;
    private ListView messageList;
    private OkHttpClient client;
    private MessageAdapter messageAdapter;
    public static WebSocket webSocket;
    private EchoWebSocketListener echoWebSocketListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http);
        Button bStartConnection = (Button) findViewById(R.id.buttonStartConnection);
        TextView send = findViewById(R.id.send);
        messageBox = (TextView) findViewById(R.id.messageBox);
        messageList = (ListView) findViewById(R.id.messageList);
        bTestSwitch = (Button) findViewById(R.id.buttonSwitch);
        client = new OkHttpClient();

        messageAdapter = new MessageAdapter();
        messageList.setAdapter(messageAdapter);

        bStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });

        bTestSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         String message = messageBox.getText().toString();
                                         if (!message.isEmpty()) {      //if message not empty, then send it to server
                                             webSocket.send(message); //send to server
                                             messageBox.setText("");

                                             /*JSONObject jsonObject = new JSONObject();
                                             try {
                                                 jsonObject.put("message", message); //2 values, that are send to server; 1:message
                                                 jsonObject.put("byServer", false);   //2: boolean which tell if message is send by Server

                                                 //add Data to messageList
                                                 messageAdapter.addItem(jsonObject);
                                             } catch (JSONException e) {
                                                 e.printStackTrace();
                                             }*/
                                         }
                                     }



                                     });}

                                     private void start() {
                                         OkHttpClient client = new OkHttpClient();
                                         Request request = new Request.Builder().url("ws://echo.websocket.org").build();
                                         EchoWebSocketListener listener = new EchoWebSocketListener(this);
                                         webSocket = client.newWebSocket(request, listener);

                                         client.dispatcher().executorService().shutdown();
                                     }

                                     private void output(final String txt) {
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 messageBox.setText(messageBox.getText().toString() + "\n\n" + txt);
                                             }
                                         }); }
  }
/*public class MainActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private MessageAdapter messageAdapter;

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView messageList = findViewById(R.id.messageList);
        final EditText messageBox = findViewById(R.id.messageBox);
        TextView send = findViewById(R.id.send);

        instantiateWebSocket();

        messageAdapter = new MessageAdapter();
        messageList.setAdapter(messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageBox.getText().toString();
                if(!message.isEmpty()){      //if message not empty, then send it to server
                    webSocket.send(message); //send to server
                    messageBox.setText("");

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", message); //2 values, that are send to server; 1:message
                        jsonObject.put("byServer", false);   //2: boolean which tell if message is send by Server

                        //add Data to messageList
                        messageAdapter.addItem(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




        private void instantiateWebSocket () {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("ws://192.168.178.41:8080").build();
            SocketListener socketListener = new SocketListener(this);
            webSocket = client.newWebSocket(request, socketListener);  //1 Param is request, 2 param is listener, will be build later
        }


    public class SocketListener extends WebSocketListener {

        public MainActivity activity;

        public SocketListener(MainActivity activity) {
            this.activity = activity;
        }

        //is called when connection establishes with the server
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

        //is called when there is a new message (either bytes or strings
        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message", text);
                        jsonObject.put("Server", true);

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

    public class MessageAdapter extends BaseAdapter {

        List<JSONObject> messageList = new ArrayList<>();
        private Context context;

        @Override
        public int getCount() {
            return messageList.size();
        }

        @Override
        public Object getItem(int i) {
            return messageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            if(view==null)
                view = mInflater.inflate(R.layout.message_list_item, viewGroup, false);

            TextView sentMessage = view.findViewById(R.id.sentMessage);
            TextView receivedMessage = view.findViewById(R.id.receiveMessage);

            JSONObject item = messageList.get(i);

            try{
                if(item.getBoolean("byServer")) {
                    receivedMessage.setVisibility(View.VISIBLE);
                    receivedMessage.setText(item.getString("message"));
                    sentMessage.setVisibility(View.INVISIBLE);
                } else {
                    sentMessage.setVisibility(View.VISIBLE);
                    sentMessage.setText(item.getString("message"));
                    sentMessage.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return view;
        }


        void addItem(JSONObject item){
            messageList.add(item);
            notifyDataSetChanged();
        }
    }




    @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

*/





//Auth Base 64: https://192.168.0.11/endpoints/call?key=CO@1_1_1&method=get&authorization=BASE64(USERNAME:PASSWORD)
//Auth Klartext: https://192.168.0.11/endpoints/call?key=CO@1_1_1&method=get&user=USERNAME&pw=PASSWORD