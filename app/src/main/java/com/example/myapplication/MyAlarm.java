package com.example.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Belal on 8/29/2017.
 */

//class extending the Broadcast Receiver
public class MyAlarm extends BroadcastReceiver {
    //the method will be fired when the alarm is triggerred
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentReg = new Intent();
        intentReg.setClassName("com.example.myapplication","com.example.myapplication.TriviaActivity");
        intentReg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.out.println("Put extra: " + intent.getStringExtra("Difficult"));
        intentReg.putExtra("Difficult", intent.getStringExtra("Difficult"));
//        intentReg.putExtra("MyClass", (Serializable) context);
        context.startActivity(intentReg);
    }

}
