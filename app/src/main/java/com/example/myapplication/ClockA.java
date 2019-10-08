package com.example.myapplication;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;

public class ClockA implements Serializable {
    Integer Hours;
    Integer Minites;
    String AMPM;
    ArrayList<String> Days = new ArrayList<String>();
    String Difficult = "Any";

    public Integer getHours(){
        return this.Hours;
    }

    public Integer getMinites(){
        return this.Minites;
    }
    public String getDifficult(){return this.Difficult;}
    public String getAMPM(){
        return this.AMPM;
    }

    public ArrayList<String> getDays(){return this.Days;}
    public void setDifficult(String Difficule){this.Difficult = Difficule;}
    public void setHourse(Integer Hourse){
        this.Hours = Hourse;
    }
    public void setMinites(Integer Minites){
        this.Minites = Minites;
    }
    public void setAMPM(String AMPM){
        this.AMPM = AMPM;
    }
    public void setDays(String day){
        Days.add(day);
    }
}
