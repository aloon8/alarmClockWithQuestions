package com.example.myapplication;

public class Question {

    private String   question;
    private String   correctAnswer;
    private String[] answers;

    public Question(){}
    public Question(String Question, String[] answers, String correctAnswer) {
        this.question      = Question;
        this.answers       = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getTextQuestion(){
        return question;
    }

    public String getAnswer1(){
        return answers[0];
    }
    public String getAnswer2(){
        return answers[1];
    }
    public String getAnswer3(){
        return answers[2];
    }
    public String getAnswer4(){
        return answers[3];
    }

    public String getCorrectAnswer(){
        return correctAnswer;
    }
}



