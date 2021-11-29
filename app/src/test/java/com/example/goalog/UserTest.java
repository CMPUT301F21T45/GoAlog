package com.example.goalog;


import static org.junit.Assert.*;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;


/**
 *  Unit Test to Test User Class to ensure that all public function works
 *  except the setToFirebase()
 */

public class UserTest {

    private User testUser;

    @Before
    public void setUp() {
        testUser = new User("userID", "user@email.com", "TestUserName");
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("TestUserName", testUser.getDisplayName());
    }

    @Test
    public void testSetDsiplayName() {
        String userName = "Hameem";

        testUser.setDisplayName(userName);
        // check to ensure the setter has properly passed the correct name to the getter
        assertEquals(userName, testUser.getDisplayName());
    }

    @Test
    public void testIsCreated() {
        // created is false by default
        assertFalse(testUser.isCreated());
    }

    @Test
    public void testSetCreated() {
        testUser.setCreated(Boolean.TRUE);
        assertTrue(testUser.isCreated());
    }

    @Test
    public void testGetEmail() {
        assertEquals("user@email.com", testUser.getEmail());
    }




}