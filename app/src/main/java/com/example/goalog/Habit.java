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
    /**
     * A habit consists of the following attribute:
     *     [habitTitle] is the title of the habit, and has a length constraint 20 character when editing it.
     *     [habitReason] is motivation, and has length constraint of 30 character;
     *     [startDate] in yyyy-mm-dd format;
     *     [weekdayPlan] "123" if want this habit done on Monday, Tuesday, Wednesday;
     *     [isPublic] whether this object is public to others;
     *     [habitID] is a unique id for each habit;
     */

    private String habitTitle;
    private String habitReason;
    private String startDate;
    private String weekdayPlan;
    private boolean isPublic;
    private String habitID;

    //constructor
    public Habit(String habitTitle, String habitReason, String startDate, String weekdayPlan, boolean isPublic,String habitID){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
        this.weekdayPlan = weekdayPlan;
        this.isPublic = isPublic;
        this.habitID = habitID;
    }

    //getters and setters
    public void setHabitReason(String habitReason) {
        if (habitReason.length() > 30) {
            this.habitReason = habitReason.substring(0,30);
        }
    }

    public String getHabitTitle() {
        return habitTitle;
    }

    public void setHabitTitle(String habitTitle) {
        if (habitTitle.length() > 20) {
            this.habitTitle = habitTitle.substring(0,30);
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
