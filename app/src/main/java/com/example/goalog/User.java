package com.example.goalog;

import java.util.ArrayList;

public class User {
    private String loginName;
    private String publicName;
    private String password;
    private ArrayList<Habit> habits;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public void setHabits(ArrayList<Habit> habits) {
        this.habits = habits;
    }
}
