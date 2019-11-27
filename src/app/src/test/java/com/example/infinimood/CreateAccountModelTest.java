package com.example.infinimood;

import com.example.infinimood.model.CreateAccountModel;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CreateAccountModelTest {

    private void testByName(String name, Object data) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final CreateAccountModel model = new CreateAccountModel();
        final Method getterMethod = CreateAccountModel.class.getMethod("get" + name);
        final Method setterMethod = CreateAccountModel.class.getMethod("set" + name, data.getClass());

        setterMethod.invoke(model, data);
        assertEquals(data, getterMethod.invoke(model));
        assertTrue(model.hasChanged());

        model.notifyObservers();
        setterMethod.invoke(model, data);
        assertFalse(model.hasChanged());
    }

    private void testByNameNoThrow(String name, Object data) {
        try {
            testByName(name, data);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            fail();
        }
    }

    @Test
    public void testSetUsername() {
        testByNameNoThrow("Username", "tester");
    }

    @Test
    public void testSetEmail() {
        testByNameNoThrow("Email", "test@example.com");
    }

    @Test
    public void testSetPassword() {
        testByNameNoThrow("Password", "654321");
    }

    @Test
    public void testSetPasswordRepeat() {
        testByNameNoThrow("PasswordRepeat", "098765");
    }

}
