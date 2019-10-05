package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClockDisplay extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TimePicker alarmTime;
    TextClock  currentTime;
    Button B;
    ClockA myclock;
    JSONObject clocks;
    static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("IMMMMMMMM HEREEEEEE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_display);

        alarmTime = findViewById(R.id.TimePicker);
        currentTime = findViewById(R.id.TextClock);
        B = findViewById(R.id.button);
        myclock = new ClockA();
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext() , RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myclock.Hours = alarmTime.getCurrentHour();
                myclock.Minites = alarmTime.getCurrentMinute();
                if(myclock.getHours()>12) {
                    myclock.Hours = myclock.Hours - 12;
                    myclock.AMPM = "PM";
                }else{
                    myclock.AMPM="AM";
                }
                System.out.println("immmm heeererereree");
                CreateFile(v.getContext());
                WriteFile(v.getContext());
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });


//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if(currentTime.getText().toString().equals(AlarmTime())){
////                    r.play();
//                    r.stop();
//                }else{
//                    r.stop();
//                }
//            }
//        },0,1000);
    }


    public void showPopupDays(View v){
        PopupMenu pop = new PopupMenu(this,v);
        pop.setOnMenuItemClickListener(this);
        pop.inflate(R.menu.popup_days);
        pop.show();
    }
    public void showPopupDifficult(View v){
        PopupMenu pop = new PopupMenu(this,v);
        pop.setOnMenuItemClickListener(this);
        pop.inflate(R.menu.popup_difficult);
        pop.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.sunday:
                Toast.makeText(this,"Sunday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Sunday");
                return true;
            case R.id.monday:
                Toast.makeText(this,"Monday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Monday");
                return true;
            case R.id.tuesday:
                Toast.makeText(this,"Tuesday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Tuesday");
                return true;
            case R.id.wednesday:
                Toast.makeText(this,"Wednesday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Wednesday");
                return true;
            case R.id.thursday:
                Toast.makeText(this,"Thursday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Thursday");
                return true;
            case R.id.friday:
                Toast.makeText(this,"Friday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Friday");
                return true;
            case R.id.saturday:
                Toast.makeText(this,"Saturday",Toast.LENGTH_SHORT).show();
                myclock.setDays("Saturday");
                return true;
            case R.id.easy:
                Toast.makeText(this,"Easy",Toast.LENGTH_SHORT).show();
                myclock.Difficult = "easy";
                System.out.println("EASY");
                return true;
            case R.id.medium:
                Toast.makeText(this,"Medium",Toast.LENGTH_SHORT).show();
                myclock.Difficult = "medium";
                return true;
            case R.id.hard:
                Toast.makeText(this,"Hard",Toast.LENGTH_SHORT).show();
                myclock.Difficult = "hard";
                return true;
            default:
                return false;
        }
    }

    public String AlarmTime(){

        Integer alarmHours = alarmTime.getCurrentHour();
        Integer alarmMinites = alarmTime.getCurrentMinute();

        String AlarmTimestring;
        if(alarmHours > 12) {
            alarmHours = alarmHours - 12;
            AlarmTimestring = alarmHours.toString().concat(":").concat(alarmMinites.toString()).concat(" PM");
        }
        else{
            AlarmTimestring = alarmHours.toString().concat(":").concat(alarmMinites.toString()).concat(" AM");
        }
        return AlarmTimestring;

    }


    static void CreateFile(Context context) {
        String filename = "storage.json";
        String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        if (!file.exists())
            try {
                FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                JSONObject ClockJsonObj = new JSONObject();
                ClockJsonObj.put("Clocks" , "");
                fos.write(ClockJsonObj.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    void WriteFile(Context context) {
        String fileInString = ReadFile(context);
        System.out.println(fileInString.toString());
        try {
            JSONObject json = new JSONObject(fileInString);
            System.out.println("json: " + json);
            clocks = json;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(clocks.toString().length());


        JSONObject clock1 = new JSONObject();
        try {
            clock1.put("Hours", myclock.Hours);
            clock1.put("Minutes", myclock.Minites);
            clock1.put("ampm", myclock.AMPM);
            clock1.put("Difficult", myclock.Difficult);
            JSONArray myJSONArray = new JSONArray();
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            for (String day:myclock.Days) {
                array.put(day);
            }
            clock1.put("key", array);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            clocks.put("clock" + id++, clock1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonStr = clocks.toString();
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("storage.json", Context.MODE_PRIVATE);
            fos.write(jsonStr.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static String ReadFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput("storage.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("Hello " + sb.toString());
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }
}

