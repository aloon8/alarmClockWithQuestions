package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.JsonReader;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.ClockDisplay.CreateFile;
import static com.example.myapplication.ClockDisplay.DeleteJsonObject;
import static com.example.myapplication.ClockDisplay.ReadFile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Context context;
    CardView cardview;
    LayoutParams layoutparams, layoutparams1;
    TextView textview, textView1;
    LinearLayout linearLayout;
    FloatingActionButton fabPlus, fabClock;
    Animation FabOpen, FabClose, FabRClockwisw, FabRanticlockWise;
    static Map<Integer, AlarmManager> alarmManagerMap   = new HashMap<Integer, AlarmManager>();
    static Map<Integer, PendingIntent> pendingIntentMap = new HashMap<Integer, PendingIntent>();

    boolean isOpen = false;
    static int id;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        File dir = getFilesDir();
//        File file = new File(dir, "storage.json");
//        boolean deleted = file.delete();

        //Read value of id before the app was closed
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        id = settings.getInt("id", 0);
        CreateFile(this);
        context = getApplicationContext();
        linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

        CreateCardViewProgrammatically();
        fabPlus = (FloatingActionButton)findViewById(R.id.fab_plus);
        fabClock = (FloatingActionButton)findViewById(R.id.fab_clock);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRClockwisw = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRanticlockWise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    fabClock.startAnimation(FabClose);
                    fabPlus.startAnimation(FabRanticlockWise);
                    fabClock.setClickable(false);
                    isOpen = false;
                } else {
                    fabClock.startAnimation(FabOpen);
                    fabPlus.startAnimation(FabRClockwisw);
                    fabClock.setClickable(true);
                    isOpen = true;
                }
            }
        });
        fabClock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClockDisplay.class);
                intent.putExtra("id", id++);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("id", id);

        // Commit the edits!
        editor.commit();
    }


    public CardView createCardView(JSONObject object) {
        cardview = new CardView(context);
        layoutparams = new LayoutParams(LayoutParams.MATCH_PARENT ,400);
        layoutparams.topMargin = 20;
        layoutparams.leftMargin = 20;
        layoutparams.rightMargin = 20;
        cardview.setLayoutParams(layoutparams);
        cardview.setRadius(15);
        cardview.setPadding(25, 25, 25, 25);
        cardview.setCardBackgroundColor(Color.GRAY);
        cardview.setMaxCardElevation(30);
        cardview.getBackground().setAlpha(128);
        try {
            cardview.setId((int)object.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cardview.setOnClickListener(this);

        textview = new TextView(context);
        textview.setLayoutParams(layoutparams);
        textview.setText(GetAlarmTimeString(object));
        textview.setGravity(Gravity.CENTER_HORIZONTAL);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
        textview.setTextColor(Color.BLACK);
        cardview.addView(textview);
        TextView textview1 = new TextView(context);
        layoutparams1 = new LayoutParams(LayoutParams.MATCH_PARENT ,400);
        layoutparams1.topMargin = 200;
        textview1.setLayoutParams(layoutparams1);

        try {
            JSONArray DaysArray = object.getJSONArray("key");
            for (int i = 0; i < DaysArray.length(); i++ ) {
                Object day = DaysArray.get(i);
                textview1.append(day.toString() + " ");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        textview1.setGravity(Gravity.CENTER_HORIZONTAL);
        textview1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textview1.setTextColor(Color.BLACK);
        cardview.addView(textview1);
        return  cardview;
    }

    public void CreateCardViewProgrammatically(){
        String fileInString = ReadFile(context);
        JSONObject json = null;
        try {
             json = new JSONObject(fileInString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < id; i++) {
            try {
                JSONObject object = json.getJSONObject("clock" + i);
                linearLayout.addView(createCardView(object));
            } catch (JSONException e) {
                continue;
            }
        }
    }

    public String GetAlarmTimeString(JSONObject jsonObject){
        Integer alarmHours   = null;
        Integer alarmMinites = null;
        try {
            alarmHours   = (Integer) jsonObject.get("Hours");
            alarmMinites = (Integer) jsonObject.get("Minutes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String AlarmTimestring;
        String minutes;
        if (alarmMinites < 10) {
            minutes = "0" + alarmMinites.toString();
        } else {
            minutes = alarmMinites.toString();
        }
        if(alarmHours > 12) {
            alarmHours = alarmHours - 12;
            AlarmTimestring = alarmHours.toString().concat(":").concat(minutes).concat(" PM");
        }
        else{
            AlarmTimestring = alarmHours.toString().concat(":").concat(minutes).concat(" AM");
        }
        return AlarmTimestring;

    }

    @Override
    public void onClick(View v) {
        DeleteJsonObject(context, v.getId());
        linearLayout.removeView(v);
        alarmManagerMap.get(v.getId()).cancel(pendingIntentMap.get(v.getId()));
        pendingIntentMap.remove(v.getId());
        alarmManagerMap.remove(v.getId());
    }
}
