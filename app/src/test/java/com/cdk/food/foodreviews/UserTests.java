package com.cdk.food.foodreviews;

import org.junit.Test;

import java.util.Random;

import structures.User;

import static org.junit.Assert.assertEquals;

/**
 * Created by inleeh on 7/18/17.
 */

public class UserTests {

    private static final String[] CONSUMER_NAMES = {"Angel Lee", "Tristan Martin", "Nathan Wreggit",
            "David Shen", "Andrew Yu", "Jin Xie"};
    private Random r = new Random();

    @Test(expected = IllegalArgumentException.class)
    public void testUserConstructor() {
        String username = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        User user = new User(username);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getDpFilePath(), "");
        new User(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditUserName() {
        String username = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        User user = new User(username);
        username = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        user.editUsername(username);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getDpFilePath(), "");
        user.editUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditProfilePic() {
        String username = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        User user = new User(username);
        String dpFilePath = "url/to/profile/picture";
        user.editProfilePicture(dpFilePath);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getDpFilePath(), dpFilePath);
        user.editProfilePicture(null);
    }
}
