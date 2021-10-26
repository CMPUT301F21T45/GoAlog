package com.example.goalog;

import java.util.ArrayList;
import java.util.Date;

public class Habit {
    
    private int userID;
    private String habitTitle;
    private String habitReason;
    private Date startDate;
    private ArrayList<String> weekdayPlan;


    Habit(int userID) {
        this.userID = userID;


    }

    Habit(int userID, String habitTitle){
        this.userID = userID;
        this.habitTitle= habitTitle;

    }

    Habit(int userID, String habitTitle, String habitReason){
        this.userID = userID;
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
    }



    Habit(int userID, String habitTitle, String habitReason, Date startDate){
        this.userID = userID;
        this.habitTitle = habitTitle;
        this.habitReason= habitReason;
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

    public void setHabitName(String name){

        //implement constraints?
        name = this.habitTitle;


    }

    public String getHabitReason(){

        return this.habitReason;

    }


    public Date getDate(){
        return startDate;
    }






}

