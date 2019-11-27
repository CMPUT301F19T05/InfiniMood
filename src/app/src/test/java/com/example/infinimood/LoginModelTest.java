package com.example.infinimood;

import com.example.infinimood.model.LoginModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginModelTest {

    @Test
    public void testSetEmail() {
        final LoginModel model = new LoginModel();
        final String email = "test@example.com";

        model.setEmail(email);
        assertEquals(email, model.getEmail());
        assertTrue(model.hasChanged());

        model.notifyObservers();
        model.setEmail(email);
        assertFalse(model.hasChanged());
    }

    @Test
    public void testSetPassword() {
        final LoginModel model = new LoginModel();
        final String password = "123456";

        model.setPassword(password);
        assertEquals(password, model.getPassword());
        assertTrue(model.hasChanged());

        model.notifyObservers();
        model.setPassword(password);
        assertFalse(model.hasChanged());
    }

}
