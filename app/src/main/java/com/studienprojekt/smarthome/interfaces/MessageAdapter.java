package com.studienprojekt.smarthome.interfaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.studienprojekt.smarthome.R;
import com.studienprojekt.smarthome.activities.HttpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private HttpActivity httpActivity;

    List<JSONObject> messageList = new ArrayList<>();

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

        LayoutInflater mInflater = (LayoutInflater) httpActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ); //so hats früher geheißen, jetzt wie 1 drüber schreiben!!!
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