package com.cdk.food.foodreviews;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import structures.Review;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by martint on 7/17/17.
 */

public class ReviewTests {

    private static final String[] CONSUMER_NAMES = {"Angel Lee", "Tristan Martin", "Nathan Wreggit",
            "David Shen", "Andrew Yu", "Jin Xie"};
    private static final String[] RESTAURANT_NAMES = {"State Burgers", "Starbucks", "Simple Thai",
            "Bambu", "Musashi's", "Boiling Point"};
    private static final int MAX_RATING = 6;
    private Review testReview;
    private Random r = new Random();

    @Before
    public void init() {
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = r.nextInt(MAX_RATING);
        testReview = new Review(consumerName, restaurantName, rating);
    }

    @Test
    public void testNewReviewConstructor() {
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = r.nextInt(MAX_RATING);
        Review review = new Review(consumerName, restaurantName, rating);
        assertEquals(review.getConsumerName(), consumerName);
        assertEquals(review.getRestaurantName(), restaurantName);
        assertEquals(review.getRating(), rating);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRating() {
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = -1;
        Review review = new Review(consumerName, restaurantName, rating);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBeyondMaxRating() {
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = 100;
        Review review = new Review(consumerName, restaurantName, rating);
    }

    @Test
    public void testLoadReviewConstructor() {
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = r.nextInt(MAX_RATING);
        long timeStamp = 123456789;
        Review review = new Review(consumerName, restaurantName, rating, timeStamp);
        assertEquals(review.compareTo(timeStamp), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBody() {
        testReview.addBody(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPicture() {
        testReview.addPicture(null);
    }

    @Test
    public void testCompareToTimeStamp() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = r.nextInt(MAX_RATING);
        Review review = new Review(consumerName, restaurantName, rating);
        assertTrue(review.compareTo(testReview.getTimeStampMilli()) < 0);
        assertTrue(testReview.compareTo(review.getTimeStampMilli()) > 0);
    }

    @Test
    public void testCompareToReview() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        String consumerName = CONSUMER_NAMES[r.nextInt(CONSUMER_NAMES.length)];
        String restaurantName = RESTAURANT_NAMES[r.nextInt(RESTAURANT_NAMES.length)];
        int rating = r.nextInt(MAX_RATING);
        Review review = new Review(consumerName, restaurantName, rating);
        assertTrue(review.compareTo(testReview) < 0);
        assertTrue(testReview.compareTo(review) > 0);
    }

    @Test
    public void testEquals() {
        assertTrue(testReview.equals(testReview));
    }
}