package com.example.goalog;

import java.util.ArrayList;
import java.util.Date;

public class Habit {
    // Members
    //private int uid;
    //private int hid;
    //private boolean[] schedule;

    private String habitTitle;
    private String habitReason;
    private Date startDate;
    private ArrayList<String> weekdayPlan;


    /*
    We can add constraint, ask ta for confirmation.
    Habit(int userID) {
        this.userID = userID;


    }

    Habit(int userID, String habitName){
        this.userID = userID;
        this.habitTitle = habitName;

    }

    Habit(int userID, String habitName, String habitReason){
        this.userID = userID;
        this.habitTitle = habitName;
        this.habitReason = habitReason;
    }

     */


    Habit(String habitTitle, String habitReason, Date startDate){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    public String getHabitTitle(){


        return this.habitTitle;
    }

    public void setHabitTitle(String name){

        //implement constraints?
        name = this.habitTitle;


    }

    public String getHabitReason(){

        return this.habitReason;

    }


    public Date getStartDate(){
        return startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = date;
    }




}


