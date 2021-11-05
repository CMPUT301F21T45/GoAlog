package com.example.goalog;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class HabitEvent implements Serializable {


    //declaring variables

    private String userID;
    private String eventID;
    private String eventComment;
    private String completeDate;
    private String habitTitle;
    private double latitude;
    private double longitude;

    private Boolean hasLocation;

    private Boolean scheduled;



    /*** A habitevent consists of the following attribute:
     *     [habitTitle] is the title of the habit, and has a length constraint 20 character when editing it.
     *     [EventID] in string format
     *     [startDate] in yyyy-mm-dd format;
     *     [hasLocation] return the Boolean format
     *     [latitude] record the double number
     *     [habitID] record the double number
     *     [evenComment] up to 30 letter event comment
     *
     *
     */

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
    ///change the simple HabitEventList
    public HabitEvent(String eventID,String eventCommentString,String completeDate, String habitTitle) {
        this.completeDate = completeDate;
        this.eventID=eventID;
        this.habitTitle = habitTitle;
        this.eventComment=eventCommentString;
        this.hasLocation = true;

    }

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

        //will deal with scheduled stuff later   this.scheduled = h1.getScheduled();
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


}
