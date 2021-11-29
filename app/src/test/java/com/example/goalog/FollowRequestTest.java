package com.example.goalog;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


/**
 *  Unit Test to Test FollowRequest Class to ensure that all public function are working
 *  except the sendToFirebase()
 */
public class FollowRequestTest {
    private FollowRequest testFollowRequest;

    @Before
    public void setUp() {
        testFollowRequest = new FollowRequest("fromUser", "toUser", "msg");
    }


    @Test
    public void testGetFromUser() {
        assertEquals("fromUser", testFollowRequest.getFromUser());
    }

    @Test
    public void testGetToUser() {
        assertEquals("toUser", testFollowRequest.getToUser());
    }

    @Test
    public void testGetMessage() {
        assertEquals("msg", testFollowRequest.getMessage());
    }
}

