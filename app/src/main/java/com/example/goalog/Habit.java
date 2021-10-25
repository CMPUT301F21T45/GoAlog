package com.example.goalog;

import java.util.ArrayList;
import java.util.Date;

public class Habit {
    
    private int userID;
    private String habitTitle;
    private String reason;
    private Date startDate;
    private ArrayList<String> weekdayPlan;


    Habit(int userID) {
        this.userID = userID;


    }

    Habit(int userID, String habitTitle){
        this.userID = userID;
        this.habitTitle= habitTitle;

    }

    Habit(int userID, String habitTitle, String reason){
        this.userID = userID;
        this.habitTitle = habitTitle;
        this.reason = reason;
    }



    Habit(int userID, String habitTitle, String reason, Date startDate){
        this.userID = userID;
        this.habitTitle = habitTitle;
        this.reason = reason;
        this.startDate = startDate;

    }



    public int getUserID(){

        return this.userID;
    }

    public void setUserID(int uid){
        //implement constraints
        //if passes constraints
        uid = this.userID;

    }



    public String getHabitTitle(){


        return this.habitTitle;
    }

    public void habitTitle(String name){

        //implement constraints?
        name = this.habitTitle;


    }

    public String getReason(){

        return this.reason;

    }


    public Date getDate(){
        return startDate;
    }






}

