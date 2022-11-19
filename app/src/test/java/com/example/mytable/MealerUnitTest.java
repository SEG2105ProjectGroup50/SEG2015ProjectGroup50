package com.example.mytable;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import android.content.Context;

@RunWith(MockitoJUnitRunner.class)
public class MealerUnitTest {
    @Mock
    Context mMockContext;

    @Test
    public void testSettingCVC() {
        Client client = new Client(mMockContext);
        // ...when the boolean is returned from the object under test...
        Boolean result = client.setCardCVC("1234"); //wrong format, more than 3 digits
        // ...then the result should be the expected value.
        assertThat(result, is(false));

        result = client.setCardCVC("768");
        assertThat(result, is(true));
    }

    @Test
    public void testSettingUserEmail() {
        User user = new User(mMockContext);
        // ...when the boolean is returned from the object under test...
        Boolean result = user.setEmail("asdasd"); //wrong format, not valid email
        // ...then the result should be the expected value.
        assertThat(result, is(false));

        result = user.setEmail("abc@domain.com");
        assertThat(result, is(true));
    }

    @Test
    public void testSettingUserPassword() {
        User user = new User(mMockContext);
        // ...when the boolean is returned from the object under test...
        Boolean result = user.setPassword("asdasd"); //wrong format, not valid password
        // ...then the result should be the expected value.
        assertThat(result, is(false));

        result = user.setPassword("ABCabc123!"); //valid password
        assertThat(result, is(true));
    }

    @Test
    public void testSettingCardNumber() {
        Client client = new Client(mMockContext);
        // ...when the boolean is returned from the object under test...
        Boolean result = client.setCardNumber("12345678"); //wrong format, not valid card number length
        // ...then the result should be the expected value.
        assertThat(result, is(false));

        result = client.setCardNumber("1234123412341234"); //valid format, valid card num length
        assertThat(result, is(true));
    }
}