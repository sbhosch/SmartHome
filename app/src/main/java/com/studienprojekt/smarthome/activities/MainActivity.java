package com.studienprojekt.smarthome.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.studienprojekt.smarthome.Area;
import com.studienprojekt.smarthome.Device;
import com.studienprojekt.smarthome.PopUpView;
import com.studienprojekt.smarthome.R;

public class MainActivity extends AppCompatActivity {

    public Area currentArea;
    public Device currentDevice;
    private String currentDeviceName;
    private TextView progressText;
    private TextView header;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hier mit currentDevice.getName() arbeiten...
        currentDeviceName = "Küchenlampe";
        header = findViewById(R.id.header);
        header.setText(currentDeviceName);
        //Toast.makeText(getApplicationContext(), currentArea.getName(), Toast.LENGTH_LONG).show(); // Aktuellen Raum anzeigen

        // initiate a Switch
        final Switch onOffSwitch = (Switch) findViewById(R.id.simpleSwitch);

        //set the current state of a Switch; kann auch in xml erstellt werden aber hier sinnvoller da status dynamisch programmiert werden soll...
        onOffSwitch.setChecked(true);
        //displayed text of the Switch; kann auch in xml erstellt werden aber hier sinnvoller, da text dynamisch programmiert werden soll...
        onOffSwitch.setText("An/Aus");

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                String statusOnOffSwitch, statusSwitch2;
                if (onOffSwitch.isChecked())
                    statusOnOffSwitch = onOffSwitch.getTextOn().toString();
                else
                    statusOnOffSwitch = onOffSwitch.getTextOff().toString();
                Toast.makeText(getApplicationContext(), "onOffSwitch :" + statusOnOffSwitch, Toast.LENGTH_LONG).show(); // display the current state for switch's
            }
        });

        // set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        progressText = findViewById(R.id.textView);
        progressText.setText("Progress: " + progress);

        Button httpIntent = (Button)findViewById(R.id.buttonHttp);
        httpIntent.setText("Http Activity");

        httpIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HttpActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mitem=menu.findItem(R.id.spinner2);
        Spinner dropdown = findViewById(R.id.spinner1);
        setupSpinner(dropdown);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                View view = new PopUpView(this);
                onButtonShowPopupWindowClick(view);
                return(true);
            case R.id.settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_LONG).show();
                return(true);
            case R.id.exit:
                Toast.makeText(this, "Close App", Toast.LENGTH_LONG).show();
                finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    public void setupSpinner(Spinner spin){
        //wrap items in the Adapter
        ArrayAdapter<CharSequence> dropAdapter = ArrayAdapter.createFromResource(this, R.array.dropDownRooms, android.R.layout.simple_spinner_item);
        dropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //assign adapter to the Spinner
        spin.setAdapter(dropAdapter);
    }

    public void onButtonShowPopupWindowClick(View view){
        Toast.makeText(this, "This is an Application developed by several Students from Landshut!", Toast.LENGTH_LONG).show();
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.aboutpopup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            progressText.setText("Progress: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            Toast.makeText(getApplicationContext(), currentDeviceName + " wurde gedimmt", Toast.LENGTH_LONG).show(); // display the current state for switch's

        }
    };
}
