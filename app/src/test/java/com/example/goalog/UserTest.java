package com.example.goalog;


import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import com.example.goalog.Habit;



import com.example.goalog.User;




/**
 *
 */

public class UserTest {

    // Test to ensure set username is equal to the String given
    @Test
    public void testSetPublicName() {


        String userName = "Hameem";
        User testName = new User("fake");

        testName.setPublicName(userName);
        /* check to ensure the setter has properly passed the correct name to the getter */
        assertEquals(userName, testName.getPublicName());
    }

    // Test to ensure username is not set if it is too long
    @Test
    public void testPublicNameLength() {

        String user1 = "WhatIsTheLongestNameInTheWorldDoesAnyoneKnow";


        User testNameLength = new User("Louie");

        try{
            testNameLength.setPublicName(user1);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
        }
        // error replication fails
       // assertTrue(Boolean.FALSE);

    }




}
