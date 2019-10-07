package com.example.myapplication;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Questions extends AsyncTask<Void,Void,Void> {
    public Question[] questions;
    int quesNum;

    public Questions(int ques_num) {
        super();
        quesNum = ques_num;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public Question getQuestion(int index) {
        return questions[index];
    }

    @Override
    protected Void doInBackground(Void... voids) {
        questions = new Question[quesNum];
        String strUrl = "https://opentdb.com/api.php?amount="+String.valueOf(10)+"&type=multiple";
        try {
            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            java.lang.Thread.sleep(1000);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String data = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONObject jsonObj = new JSONObject(data);
            JSONArray JA = jsonObj.getJSONArray("results");
            String[] AllAnswerArr = new String[4];
            int randInd,internalInd;
            Random random = new Random();
            for(int i =0 ;i <JA.length(); i++){
                JSONObject JO = (JSONObject) JA.get(i);
                JSONArray arrJson = JO.getJSONArray("incorrect_answers");
                String[] ArrAnswer = new String[arrJson.length()];
                for(int j = 0; j < arrJson.length(); j++) {
                    ArrAnswer[j] = arrJson.getString(j);
                }
                String CorrAnswer = (String) JO.get("correct_answer");

                randInd = random.nextInt(4);
                internalInd = 0;
                for(int id=0; id < 4 ; id++) {

                    if(randInd == id) {
                        AllAnswerArr[id] = CorrAnswer;
                    } else {
                        AllAnswerArr[id] = ArrAnswer[internalInd++];
                    }
                }
                questions[i] = new Question((String) JO.get("question"),
                        new String[]{AllAnswerArr[0], AllAnswerArr[1], AllAnswerArr[2], AllAnswerArr[3]},
                        CorrAnswer);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
