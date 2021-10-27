package com.example.habbitc;

import android.location.Location;

import java.time.LocalDate;

public class HabitEvent implements Comparable<HabitEvent> {


    //declaring variables

    private String userID;
    private String eventID;
    private String eventComment;
    private LocalDate completeDate;
    private String habitTitle;

    private Boolean wantsLocation;

    private Boolean scheduled;

  //  private String habitName = "";
    //private String habitAttribute = "";

    /**
     * Constructor - needs a uid and hid to uniquely identify it.  eid is set upon transmission to
     * ElasticSearch.

     */

    // by default
    public HabitEvent (String userID, String habitTitle) {
        this.userID = userID;
        this.habitTitle = habitTitle;
        this.wantsLocation = False;
    }

    public void setUserID(String userID){

        this.userID = userID;
    }

    public void setHabitTitle(String habitTitle){

        this.habitTitle = habitTitle;
    }
    public void setEventID(String firebaseEvent){

        this.eventID = firebaseEvent; //maybe user ID and habit Name, combine
        //them together and parse it?

    }

    public void setLocation(Location eventLocation){
        if (eventLocation != null) {
            this.wantsLocation = true;
            this.longitude = eventLocation.getLongitude();
            this.latitude = eventLocation.getLatitude();
            this.altitude = eventLocation.getAltitude();
        }
        public void setComment(String eventComment) throw  IllegalArgumentException{
            if (eventComment.length() <= 20) {
                this.eventComment = eventComment;
            } else {
                throw new IllegalArgumentException("Even Comment cannot exceed 20 characters.");
            }
        }



        @Override
    public int compareTo(HabitEvent habitEvent) {
        return 0;
    }
}
