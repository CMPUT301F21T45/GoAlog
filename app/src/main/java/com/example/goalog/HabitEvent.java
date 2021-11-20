package com.example.goalog;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/***
 * habitEvent Class:
 *  The class represents the event the user completes for a habit. The user can freely choose
 *  to leave a comment, upload a picture or not. The class can contain the location where the user
 *  completed it.
 * Current Issues:
 *  Two Todos are denoted.
 *  The optional picture uploading process has made some progress. But it is excluded from the first
 *  half of the project. Other attributes such as ones related to locations might be used later.
 *  The commented constructors are for planning, testing, etc.
 *  We will have a final version of the constructor once we have implemented all essential
 *  functionalities. All redundant data will be removed then.
 */
public class HabitEvent implements Serializable, Comparable<HabitEvent>{
    private String userID;
    private String eventID;
    private String eventComment;
    private String completeDate;
    private String habitTitle;
    private double latitude;
    private double longitude;
    private Boolean hasLocation;

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
     *
     * Current Issues:
     *   The habitTitle has no use.
     *   Other attributes need to be included later.
     */
    public HabitEvent(String eventID,String eventCommentString,String completeDate, String habitTitle) {
        this.completeDate = completeDate;
        this.eventID=eventID;
        this.habitTitle = habitTitle;
        this.eventComment=eventCommentString;
        this.hasLocation = true;
    }

     /*
    // by default Location set as false
    public HabitEvent(Boolean hasLocation, String completeDate, String eventComment, String eventID, String habitTitle, double latitude, double longitude, String userID) {
        this.hasLocation = hasLocation;
        this.completeDate = completeDate;
        this.eventID=eventID;
        this.latitude=latitude;
        this.longitude=longitude;
        this.userID=userID;
        this.eventComment=eventComment;
        this.habitTitle = habitTitle;
        this.hasLocation = true;
    }
    public HabitEvent(Boolean hasLocation, String completeDate, String eventID, String habitTitle, double latitude, double longitude, String userID){
        this.hasLocation = hasLocation;
        this.completeDate = completeDate;
        this.eventID=eventID;
        this.latitude=latitude;
        this.longitude=longitude;
        this.userID=userID;
        this.habitTitle = habitTitle;
        this.hasLocation = true;
    }
    public HabitEvent(String completeDate, String eventComment, String eventID, String habitTitle, String userID) {
        this.completeDate = completeDate;
        this.eventID=eventID;
        this.userID=userID;
        this.eventComment=eventComment;
        this.habitTitle = habitTitle;
        this.hasLocation = true;
    }
     */
    // TODO: Upload a picture for the event.

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HabitEvent(HabitEvent h1) {

        // Copy all members over
        this.setUserID(h1.getUserID());
        this.setHabitTitle(h1.getHabitTitle());
        this.setEventID(h1.getEventID());
        this.setEventComment(h1.getEventComment());

        // this.setPhoto(h1.getPhoto()); will deal with photo stuff later
        if (h1.hasLocation()) {
            this.setLocation(h1.getLocation()); //requires API notation on Android studio
            this.hasLocation = true;
        }else {
            this.hasLocation = false;
        }

        // TODO: Location
        // this.scheduled = h1.getScheduled();
        // this.habitTitle = h1.getHabitName();

    }


    public void setUserID(String userID) {

        this.userID = userID;
    }
    public String getUserID(){

        return this.userID;
    }
    public void setHabitTitle(String habitTitle) {

        this.habitTitle = habitTitle;
    }

    public void setEventID(String firebaseEvent) {

        this.eventID = firebaseEvent; //maybe user ID and habit Name, combine
        //them together and parse it? //will find a way later

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setLocation(Location eventLocation) {
        if (eventLocation != null) {
            this.hasLocation = true;
            this.longitude = eventLocation.getLongitude();
            this.latitude = eventLocation.getLatitude();
            // this.altitude = eventLocation.getAltitude();
        }
    }

    public void setEventComment(String eventComment){
        if (eventComment.length() <= 20) {
            this.eventComment = eventComment;
        } else {
            throw new IllegalArgumentException("Even Comment cannot exceed 20 characters.");
        }
    }


    public boolean hasLocation() {
        return this.hasLocation;  // maybe no .thhis?? not sure will check later
    }

    public Location getLocation() {
        if (hasLocation) {

            Location l1 = new Location("");
            l1.setLatitude(latitude);
            l1.setLongitude(longitude);
            return l1;
        } else {
            return null;
        }
    }
    public String getEventComment(){
        return eventComment;
    }

    public String getEventID() {
        return eventID; }

    public String getCompleteDate(){
        return completeDate;
    }
    public String getHabitTitle() {
        return habitTitle; }

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
