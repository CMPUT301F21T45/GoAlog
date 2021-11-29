package com.example.goalog;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;



/**
 *  Unit Test to Test User Class to ensure that the setters are working
 *  properly for user's Display Name. Will add constraint test after discussing with
 *  members of my team
 */

public class UserTest {

    User testUser = new User(null, null, "Ninja");

    @Test
    public void testSetPublicName() {
        String userName = "Hameem";

        testUser.setDisplayName(userName);
        /* check to ensure the setter has properly passed the correct name to the getter */
        assertEquals(userName, testUser.getDisplayName());
    }

    /* TODO
     Get an answer from my teammates as to whether we should be implementing a character limit
     for User's Display Name & Modify the following method upon doing so
     */

    @Test
    public void testPublicNameLength() {
        String user1 = "What Is The Longest Name In The World Does Anyone Know";

        try{
            testUser.setDisplayName(user1);
        }
        catch (IllegalArgumentException err) {
            assertTrue(Boolean.TRUE);
        }


    }




}
