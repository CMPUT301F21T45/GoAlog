package com.example.goalog;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class HabitEvent {


    //declaring variables

    private String userID;
    private String eventID;
    private String eventComment;
    private String completeDate;
    private String habitTitle;
    private double latitude;
    private double longitude;

    private Boolean wantsLocation;

    private Boolean scheduled;



    /**
     *
     */

    // by default Location set as false

    @RequiresApi(api = Build.VERSION_CODES.O)
    public HabitEvent(HabitEvent h1) {

        // Copy all members over
        this.setUserID(h1.getUserID());
        this.setHabitTitle(h1.getHabitTitle());
        this.setEventID(h1.getEventID());
        this.setEventComment(h1.getEventComment());
        this.setCompleteDate(h1.getCompleteDate());
        // this.setPhoto(h1.getPhoto()); will deal with photo stuff later
        if (h1.wantsLocation()) {
            this.setLocation(h1.getLocation()); //requires API notation on Android studio
            this.wantsLocation = true;
        }else {
            this.wantsLocation = false;
        }

        //will deal with scheduled stuff later   this.scheduled = h1.getScheduled();
        // this.habitTitle = h1.getHabitName();


    }

    public HabitEvent(String habitEventID, String eventCommentString, String completeDate,String  habitTitle) {
        this.completeDate = completeDate;
        this.eventID=habitEventID;
        this.eventComment=eventCommentString;
        this.habitTitle = habitTitle;
        this.wantsLocation = true;

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
            this.wantsLocation = true;
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

    public void setCompleteDate ( String completeDate){
        this.completeDate = completeDate;
    }
    public boolean wantsLocation() {
        return this.wantsLocation;  // maybe no .thhis?? not sure will check later
    }

    public Location getLocation() {

        if (wantsLocation) {

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
