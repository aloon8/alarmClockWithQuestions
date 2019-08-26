package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Rectangle extends View {

    Paint mpaint, paint2;
    float w;
    float textSize;
    String s;

    public Rectangle(Context context) {
        super(context);
        mpaint= new Paint();
        mpaint.setColor(Color.RED);
        mpaint.setStyle(Paint.Style.FILL);
        paint2= new Paint();
        paint2.setColor(Color.GREEN);
        paint2.setTextSize(50);  //set text size
        s = "Hour";
        w = paint2.measureText(s)/2;
        textSize = paint2.getTextSize();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        paint2.setTextAlign(Paint.Align.CENTER);
        canvas.drawRect(300-w, 300 - textSize, 300 + w, 300, mpaint);
        canvas.drawText(s, 300, 300 ,paint2); //x=300,y=300
    }

}