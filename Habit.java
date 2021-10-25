package com.example.goalog;


import java.util.ArrayList;
import java.util.Date;

public class Habit {

    // Members
    //private int uid;
    //private int hid;
    //private boolean[] schedule;
    private int userID;
    private String habbitName;
    private String habbitReason;
    private Date startDate;
    private ArrayList<String> weekdayPlan;


    Habit(int userID) {
        this.userID = userID;


    }

    Habit(int userID, String habbitName){
        this.userID = userID;
        this.habbitName = habbitName;

    }

    Habit(int userID, String habbitName, String habbitReason){
        this.userID = userID;
        this.habbitName = habbitName;
        this.habbitReason = habbitReason;
    }



    Habit(int userID, String habbitName, String habbitReason, Date startDate){
        this.userID = userID;
        this.habbitName = habbitName;
        this.habbitReason = habbitReason;
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



    public String getHabbitName(){


        return this.habbitName;
    }

    public void setHabbitName(String name){

        //implement constraints?
        name = this.habbitName;


    }

    public String getHabbitReason(){

        return this.habbitReason;

    }


    public Date getDate(){
        return startDate;
    }






}
