package com.example.goalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Habit Class
 *  The class represents a habit the user wants to do regularly to form a good lifestyle.
 *  It records a title, a reason, a date to start, a weekly plan, and your permission or denial to
 *  display the habit to  the public.
 */
public class Habit implements Serializable, Comparable<Habit> {

    private String habitTitle;
    private String habitReason;
    private String startDate;
    private String weekdayPlan;
    private boolean isPublic;
    private String habitID;
    private Long orderID;
    private String latestFinishDate;

    /**
     * Habit Constructor:
     * @param habitTitle
     *   It is the title of the habit, and has a length constraint 20 character when editing it.
     * @param habitReason
     *   is motivation, and has length constraint of 30 character;
     * @param startDate
     *   Date to Start. It is a String in "yyyy-MM-dd" format;
     * @param weekdayPlan
     *   is a string contains the active weekdays scheduled for the habits.
     * @param isPublic
     *   whether this object is public to others;
     * @param habitID
     *   is a unique ID for each habit
     */
    public Habit(String habitTitle, String habitReason, String startDate, String weekdayPlan, boolean isPublic,String habitID){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
        this.weekdayPlan = weekdayPlan;
        this.isPublic = isPublic;
        this.habitID = habitID;
    }

    public Habit(String habitTitle, String habitReason, String startDate, String weekdayPlan, boolean isPublic, String habitID, long orderID, String latestFinishDate){
        this.habitTitle = habitTitle;
        this.habitReason = habitReason;
        this.startDate = startDate;
        this.weekdayPlan = weekdayPlan;
        this.isPublic = isPublic;
        this.habitID = habitID;
        this.orderID = orderID;
        this.latestFinishDate = latestFinishDate;
    }

    public void setHabitTitle(String habitTitle) {
        if (habitTitle.length() > 20) {
            this.habitTitle = habitTitle.substring(0,20);
        }
        else{
            this.habitTitle = habitTitle;
        }
    }

    public void setStartDate(String date) {
        this.startDate = date;
    }

    public void setLatestFinishDate(String latestFinishDate) {
        this.latestFinishDate = latestFinishDate;
    }

    public void setHabitReason(String habitReason) {
        if (habitReason.length() > 30) {
            this.habitReason = habitReason.substring(0,30);
        }
        else {
            this.habitReason = habitReason;
        }
    }


    public void setWeekdayPlan(String weekdayPlan) {
        this.weekdayPlan = weekdayPlan;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public boolean isPublic() {
        return isPublic;
    }


    public String getHabitTitle() {
        return habitTitle;
    }

    public String getHabitReason(){
        return habitReason;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getWeekdayPlan() {
        return weekdayPlan;
    }

    public String getHabitID() {
        return habitID;
    }

    public Long getOrderID() {
        return orderID;
    }

    public String getLatestFinishDate() {
        return latestFinishDate;
    }

    @Override
    public int compareTo(Habit habit) {
        Long diff = this.getOrderID() - habit.getOrderID();
        return diff.intValue();
    }
}
