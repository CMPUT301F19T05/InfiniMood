package com.example.infinimood;

import com.example.infinimood.model.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private static final String TEST_ID = "user1";
    private static final String TEST_USERNAME = "joe";

    private User createUserClass() {
        return new User(TEST_ID, TEST_USERNAME, false, false, false, false);
    }

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
        final User user = createUserClass();
        user.setCurrentUserFollows(true);
        assertTrue(user.isCurrentUserFollows());
    }

    @Test
    public void testSetCurrentUserRequestedFollow() {
        final User user = createUserClass();
        user.setCurrentUserRequestedFollow(true);
        assertTrue(user.isCurrentUserRequestedFollow());
    }

    @Test
    public void testSetFollowsCurrentUser() {
        final User user = createUserClass();
        user.setFollowsCurrentUser(true);
        assertTrue(user.isFollowsCurrentUser());
    }

    @Test
    public void testSetRequestedFollowCurrentUser() {
        final User user = createUserClass();
        user.setRequestedFollowCurrentUser(true);
        assertTrue(user.isRequestedFollowCurrentUser());
    }

}
