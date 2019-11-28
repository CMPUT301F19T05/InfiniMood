package com.example.infinimood;

import com.example.infinimood.model.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private static final String TEST_ID = "user1";
    private static final String TEST_USERNAME = "joe";

    @Test
    public void testUserSimpleConstructor() {
        final User user = new User(TEST_ID, TEST_USERNAME);
        assertEquals(TEST_ID, user.getUserID());
        assertEquals(TEST_USERNAME, user.getUsername());
        assertFalse(user.isCurrentUserFollows());
        assertFalse(user.isCurrentUserRequestedFollow());
        assertFalse(user.isFollowsCurrentUser());
        assertFalse(user.isRequestedFollowCurrentUser());
    }

    @Test
    public void testUserFullConstructor() {
        final User user = new User(TEST_ID, TEST_USERNAME, true, true, true, true);
        assertEquals(TEST_ID, user.getUserID());
        assertEquals(TEST_USERNAME, user.getUsername());
        assertTrue(user.isCurrentUserFollows());
        assertTrue(user.isCurrentUserRequestedFollow());
        assertTrue(user.isFollowsCurrentUser());
        assertTrue(user.isRequestedFollowCurrentUser());
    }

    @Test
    public void testSetCurrentUserFollows() {

    }

}
