package com.cdk.food.foodreviews;

import org.junit.Test;

import java.util.Random;

import structures.Restaurant;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Created by inleeh on 7/18/17.
 */

public class RestaurantTests {

    private static final String[] RESTAURANT_NAMES = {"State Burgers", "Starbucks", "Simple Thai",
            "Bambu", "Musashi's", "Boiling Point"};
    private Random r = new Random();

    @Test(expected = IllegalArgumentException.class)
    public void testRestaurantConstructor() {
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        double lat = 123.4567890;
        double lng = 1.234567890;
        Restaurant rest = new Restaurant(restaurantName, lat, lng);
        assertEquals(rest.getRestaurantName(), restaurantName);
        assertEquals(rest.getLatitude(), lat);
        assertEquals(rest.getLongitude(), lng);
        new Restaurant(null, lat, lng);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEditImage() {
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        double lat = 123.4567890;
        double lng = 1.234567890;
        Restaurant rest = new Restaurant(restaurantName, lat, lng);
        String photoURL = "photo/url";
        rest.editImage(photoURL);
        assertEquals(photoURL, rest.getPhotoUrl());
        rest.editImage(null);
    }

    @Test
    public void testEquals() {
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        double lat = 123.4567890;
        double lng = 1.234567890;
        Restaurant rest = new Restaurant(restaurantName, lat, lng);
        assertEquals(rest, rest);
    }
}
