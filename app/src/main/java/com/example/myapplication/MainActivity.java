package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//    static Map<Integer, AlarmManager> alarmManagerMap   = new HashMap<Integer, AlarmManager>();
//    static Map<Integer, PendingIntent> pendingIntentMap = new HashMap<Integer, PendingIntent>();

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
        LinearLayout.LayoutParams buttonLayoutParams = new LayoutParams(100, 100);
        buttonLayoutParams.leftMargin = 920;
        buttonLayoutParams.topMargin = 10;
        layoutparams = new LayoutParams(LayoutParams.MATCH_PARENT ,400);
        layoutparams.topMargin = 10;
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
        ImageButton imgButton = new ImageButton(context);
        imgButton.setImageResource(R.drawable.closebutton);
        imgButton.setLayoutParams(buttonLayoutParams);
        try {
            imgButton.setId((int)object.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imgButton.setBackgroundColor(Color.TRANSPARENT);
        imgButton.setOnClickListener(this);
        cardview.addView(imgButton);
        textview = new TextView(context);
        textview.setLayoutParams(layoutparams);
        textview.setText(GetAlarmTimeString(object));
        textview.setGravity(Gravity.CENTER_HORIZONTAL);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
        textview.setTextColor(Color.BLACK);
        cardview.addView(textview);
        TextView textview1 = new TextView(context);
        layoutparams1 = new LayoutParams(LayoutParams.MATCH_PARENT ,400);
        layoutparams1.topMargin = 160;
        textview1.setLayoutParams(layoutparams1);

        try {
            JSONArray DaysArray = object.getJSONArray("key");
            if (DaysArray.length() == 0 || DaysArray.length() == 7) {
                textview1.append("Every Day");
            } else {
                for (int i = 0; i < DaysArray.length(); i++) {
                    Object day = DaysArray.get(i);
                    textview1.append(day.toString() + " ");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        textview1.setGravity(Gravity.CENTER_HORIZONTAL);
        textview1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textview1.setTextColor(Color.BLACK);
        cardview.addView(textview1);

        //Difficult
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams difficultLayout = new LayoutParams(LayoutParams.MATCH_PARENT ,100);
        difficultLayout.topMargin = 290;
        linearLayout.setLayoutParams(difficultLayout);
        String difficultStr = "";
        try {
            difficultStr = (String) object.get("Difficult");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView[] imageViews;
        if (difficultStr.equals("Any")) {
            imageViews = new ImageView[3];
            imageViews[0] = SetImageView(context, "easy");
            imageViews[1] = SetImageView(context, "medium");
            imageViews[2] = SetImageView(context, "hard");
        } else {
            imageViews = new ImageView[1];
            imageViews[0] = SetImageView(context, difficultStr);
        }

        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i = 0; i < imageViews.length; i++) {
            linearLayout.addView(imageViews[i]);
        }
        cardview.addView(linearLayout);
        return  cardview;
    }

    public ImageView SetImageView(Context context, String difficult) {
        ImageView imageView = new ImageView(context);
        if (difficult.equals("easy")) imageView.setImageResource(R.drawable.easy_icon);
        else if (difficult.equals("medium")) imageView.setImageResource(R.drawable.medium_icon);
        else if (difficult.equals("hard")) imageView.setImageResource(R.drawable.hard_icon);
        imageView.setBackgroundColor(Color.TRANSPARENT);
        return imageView;
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
        String AlarmTimestring = null;
        String minutes;
        if (alarmMinites < 10) {
            minutes = "0" + alarmMinites.toString();
        } else {
            minutes = alarmMinites.toString();
        }
        try {
            if(jsonObject.get("ampm").toString().equals("PM")) {
                alarmHours = alarmHours + 12;
                AlarmTimestring = alarmHours.toString().concat(":").concat(minutes);
            }
            else{
                AlarmTimestring = alarmHours.toString().concat(":").concat(minutes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return AlarmTimestring;

    }

    @Override
    public void onClick(View v) {
        DeleteJsonObject(context, v.getId());
        Intent i = new Intent(this, MyAlarm.class);
        //creating a pending intent using the intent
        PendingIntent.getBroadcast(this, v.getId(), i, 0).cancel();
        // do what you want with imageView
        if (v instanceof ImageButton) {
            System.out.println("id: " + v.getId());
            linearLayout.removeView(findViewById(v.getId()));
        } else if (v instanceof CardView) {
            linearLayout.removeView(findViewById(v.getId()));
            Intent intent = new Intent(this, ClockDisplay.class);
            intent.putExtra("id", id++);
            this.startActivity(intent);
        }
    }
}
