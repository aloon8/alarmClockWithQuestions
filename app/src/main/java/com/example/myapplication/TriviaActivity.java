package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class TriviaActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_one, btn_two, btn_three, btn_four;
    TextView tv_question;
    int Score = 0;
    private Questions questions;
    private int q_index = 0;
    private int NumOfQues=100;
    private String userN;
    private String answer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
        Intent intent = getIntent();
        String difficult = intent.getStringExtra("Difficult");
        questions = new Questions(NumOfQues, difficult);

        try {
            questions.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btn_one = (Button)findViewById(R.id.button1);
        btn_one.setOnClickListener(this);
        btn_two = (Button)findViewById(R.id.button2);
        btn_two.setOnClickListener(this);
        btn_three = (Button)findViewById(R.id.button3);
        btn_three.setOnClickListener(this);
        btn_four = (Button)findViewById(R.id.button4);
        btn_four.setOnClickListener(this);

        tv_question = (TextView)findViewById(R.id.tv_question);

        NextQuestion(q_index++,false);
    }

    @Override
    public void onClick(View v) {
        boolean flag = false;
        switch (v.getId()){
            case R.id.button1:
                if(btn_one.getText() == answer){
                    flag = true;
                }
                break;
            case R.id.button2:
                if(btn_two.getText() == answer){
                    flag = true;
                }
                break;
            case R.id.button3:
                if(btn_three.getText() == answer){
                    flag = true;
                }
                break;
            case R.id.button4:
                if(btn_four.getText() == answer){
                    flag = true;
                }
                break;
        }
        NextQuestion(q_index++,flag);
    }


    private void NextQuestion(int num,boolean flag){
        if(flag){
            new AlertDialog.Builder(this)
                    .setTitle("Currect Answer")
                    .setMessage("Wake Up lazy")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
                                startActivity(intent);
                        }
                    })
                    .show();
            mediaPlayer.stop();
            return;
        }
        Question q = questions.questions[num];
        tv_question.setText(q.getTextQuestion());
        btn_one.setText(q.getAnswer1());
        btn_two.setText(q.getAnswer2());
        btn_three.setText(q.getAnswer3());
        btn_four.setText(q.getAnswer4());

        answer = q.getCorrectAnswer();
    }

}
