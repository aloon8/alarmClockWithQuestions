package com.example.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.annotation.RequiresPermission;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.FutureTask;

import static com.example.myapplication.ClockDisplay.ReadFile;


public class ClockDisplay extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TimePicker alarmTime;
    TextClock  currentTime;
    Button B,daysButton;
    ClockA myclock;
    JSONObject clocks;
    int id;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_display);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        System.out.println("id: " + id);
        alarmTime = findViewById(R.id.TimePicker);
        currentTime = findViewById(R.id.TextClock);
        B = findViewById(R.id.button);

        //Days list view
        listItems = getResources().getStringArray(R.array.Days);
        checkedItems = new boolean[listItems.length];
        daysButton = findViewById(R.id.daysButton);
        daysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ClockDisplay.this);
                mBuilder.setTitle("Days Picker");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            }

                        } else if (mUserItems.contains(position)) {
                            mUserItems.remove((Object) position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        myclock.Days.clear();
                        for (int i = 0; i < mUserItems.size(); i++) {
                            myclock.setDays(listItems[mUserItems.get(i)].substring(0,3));
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        myclock = new ClockA();
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
                WriteFile(v.getContext(), alarmTime);

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void SetAlarmManager(Context context, TimePicker timePicker, int id) {
        Calendar calendar = Calendar.getInstance();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getHour(), timePicker.getMinute(), 0);
        } else {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
        }

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        String fileInString = ReadFile(context);
        JSONObject json = null;
        JSONObject object = null;
        String difficult = "";
        try {
            json = new JSONObject(fileInString);
            object = json.getJSONObject("clock" + id);
            difficult = (String) object.get("Difficult");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);
        i.putExtra("Difficult", difficult);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, id, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
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

    void WriteFile(Context context, TimePicker timePicker) {
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
            clock1.put("id", id);
            clock1.put("Hours", myclock.Hours);
            clock1.put("Minutes", myclock.Minites);
            clock1.put("ampm", myclock.AMPM);
            clock1.put("Difficult", myclock.Difficult);
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
            clocks.put("clock" + id, clock1);
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
        SetAlarmManager(context, timePicker, id);

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

    static void DeleteJsonObject(Context context, int id) {
        String fileInString = ReadFile(context);
        System.out.println(fileInString.toString());
        JSONObject json = null;
        try {
            json = new JSONObject(fileInString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        json.remove("clock" + id);
        System.out.println("DeleteJson: " + json.toString());
        String jsonStr = json.toString();
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

    static void EditJsonObject(Context context, int id) {
        DeleteJsonObject(context, id);

    }
}

