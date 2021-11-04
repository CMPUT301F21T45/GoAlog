package com.example.goalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Habit implements Serializable {
    // Members
    //private int uid;
    //private int hid;
    //private boolean[] schedule;

    private String habitTitle;
    private String habitReason;
    private String startDate;
    private String weekdayPlan;
    private boolean isPublic;


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


    public Habit(String habitTitle, String habitReason, String startDate, String weekdayPlan, boolean isPublic){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
        this.weekdayPlan = weekdayPlan;
        this.isPublic = isPublic;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }


    public String getHabitTitle() {
        return habitTitle;
    }

    public void setHabitTitle(String name){

        //implement constraints?
        name = this.habitTitle;


    }

    public String getHabitReason(){

        return habitReason;

    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getWeekdayPlan() {
        return weekdayPlan;
    }

    public void setStartDate(String date) {
        this.startDate = date;
    }




}
