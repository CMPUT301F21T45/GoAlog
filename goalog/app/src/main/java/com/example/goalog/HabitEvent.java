package com.example.goalog;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
public class HabitEvent {


    //declaring variables

    private String userID; //has to be unique
    private String eventID; // has to be unique as well(haven't figured out how to generate it yet)
    private String eventComment; //optional event comment
    private LocalDate completeDate; // The date the event is completed
    private String habitTitle; // Name set by the user?
    // User can turn on location tracking for their events. Default = off
    private Boolean wantsLocation;

    private double latitude; // latitude of the location
    private double longitude; // longitude of the location
    /* Use only if needed. Longitude and latitude should be enough
    private double altitude;
    */
    /*
    will deal with schedule stuff later
    private Boolean scheduled;
     */



    /**
     *
     */

    // By default Location set as false. Add more constructors as required
    public HabitEvent(String userID, String habitTitle) {
        this.userID = userID;
        this.habitTitle = habitTitle;
        this.wantsLocation = false;
    }

    //Location import requires a higher level of API than 16 (i think 21?)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public HabitEvent(HabitEvent h1) {

        // The required variables being attached to a habit event
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
       // this.habitTitle = h1.getHabitName(); Ignore this


    }

    //Setter for userID
    public void setUserID(String userID) {

        this.userID = userID;
    }
    //Getter for userID
    public String getUserID(){

        return this.userID;
    }

    //Setter for HabitTitle
    public void setHabitTitle(String habitTitle) {

        this.habitTitle = habitTitle;
    }

    //Getter for HabitTitle
    public String getHabitTitle() {
        return habitTitle;
    }

    //setter for EventID, haven't figured out how to uniquely identify evenID yet
    public void setEventID(String firebaseEvent) {

        this.eventID = firebaseEvent; //maybe user ID and habit Name, combine
        //them together and parse it? //will find a way later

    }

    public String getEventID() {
        return eventID;
    }
    // The Location android import requires a higher minimum API than 16
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setLocation(Location eventLocation) {
        if (eventLocation != null) {
            this.wantsLocation = true;
            this.longitude = eventLocation.getLongitude();
            this.latitude = eventLocation.getLatitude();
            /* will only use altitude if required later. Commenting it out for now
            this.altitude = eventLocation.getAltitude();
            */
        }

    }


    public void setEventComment(String eventComment){
            if (eventComment.length() <= 20) {
                this.eventComment = eventComment;
            } else {
                throw new IllegalArgumentException("Even Comment cannot exceed 20 characters.");
            }
        }

        public void setCompleteDate ( LocalDate completeDate){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (completeDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Completion date must be on or before today's date.");
                } else {
                    this.completeDate = completeDate;
                }
            }
        }
    public boolean wantsLocation() {
        return this.wantsLocation;  // either the user wants location tracking or doesn't
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




    public LocalDate getCompleteDate(){
        return completeDate;
    }



}
