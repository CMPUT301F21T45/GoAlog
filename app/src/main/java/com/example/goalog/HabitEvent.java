package com.example.goalog;

import android.location.Location;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

/***
 * habitEvent Class:
 *  The class represents the event the user completes for a habit. The user can freely choose
 *  to leave a comment, upload a picture or not. The class can contain the location where the user
 *  completed it.
 * Current Issues:
 *  The optional picture uploading process has made some progress. But it is excluded from the first
 *  half of the project. Other attributes such as ones related to locations might be used later.
 *  The commented constructors are for planning, testing, etc.
 *  We will have a final version of the constructor once we have implemented all essential
 *  functionalities. All redundant data will be removed then.
 */
public class HabitEvent implements Serializable, Comparable<HabitEvent>{
    private String eventID;
    private String eventComment;
    private String completeDate;
    private String habitTitle;
    private String image;
    private HashMap Location;

    /**
     * The HabitEvent Constructor that is currently in use
     * @param eventID
     *   Unique ID for a Habit Event
     * @param eventCommentString
     *   A comment made to the event. <=30 char
     * @param completeDate
     *   The day when the user completes the event. It is a String in the "yy-MM-dd" format.
     * @param habitTitle
     *   The title of the parent habit.
     * @param image
     *   The image uri
     * @param location
     *  The longitude and latitude of location
     *
     */
    public HabitEvent(String eventID, String eventCommentString, String completeDate, String habitTitle, String image, HashMap location) {
        this.completeDate = completeDate;
        this.eventID=eventID;
        this.habitTitle = habitTitle;
        this.eventComment=eventCommentString;
        this.image = image;
        this.Location = location;
    }

    public String getEventID() {
        return eventID;
    }

    public String getHabitTitle() { return habitTitle; }

    public void setHabitTitle(String habitTitle) { this.habitTitle = habitTitle; }

    public String getCompleteDate(){
        return completeDate;
    }

    public HashMap getLocation() { return Location; }

    public void setLocation(HashMap location) {this.Location = location;}


    public String getEventComment() {
        return eventComment;
    }

    public void setEventComment(String eventComment){
        if (eventComment.length() <= 20) {
            this.eventComment = eventComment;
        } else {
            throw new IllegalArgumentException("Event Comment cannot exceed 20 characters.");
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    /**
     * CompareTo The method that will be used to compare the order of a DateString.
     * @param he
     *  The candidate HabitEvent object
     * @return
     *  The order of the object. It returns -2 if parse errors happen.
     */
    @Override
    public int compareTo(HabitEvent he) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(this.getCompleteDate()).compareTo(sdf.parse(he.getCompleteDate()));
        }
        catch (ParseException e) {
            e.printStackTrace();
            return -2;
        }
    }


}
