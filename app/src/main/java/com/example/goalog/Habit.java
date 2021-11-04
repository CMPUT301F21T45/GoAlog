package com.example.goalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
    private String habitID;

    public Habit(String habitTitle, String habitReason, String startDate, String weekdayPlan, boolean isPublic,String habitID){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
        this.weekdayPlan = weekdayPlan;
        this.isPublic = isPublic;
        this.habitID = habitID;
    }

    public void setHabitReason(String habitReason) {
        if (habitReason.length() <= 30) {
            this.habitReason = habitReason;
        }
    }

    public String getHabitTitle() {
        return habitTitle;
    }

    public void setHabitTitle(String habitTitle) {
        if (habitTitle.length() <= 20) {
            this.habitTitle = habitTitle;
        }
    }

    public void setWeekdayPlan(String weekdayPlan) {
        this.weekdayPlan = weekdayPlan;
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

    public String getHabitID() {
        return habitID;
    }

}
