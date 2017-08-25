package com.cdk.food.foodreviews;

import org.junit.Test;

import java.util.Random;

import structures.Review;
import structures.Reviews;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by inleeh on 7/18/17.
 */

public class ReviewsTest {
    private static final String[] CONSUMER_NAMES = {"Angel Lee", "Tristan Martin", "Nathan Wreggit",
            "David Shen", "Andrew Yu", "Jin Xie"};
    private static final String[] RESTAURANT_NAMES = {"State Burgers", "Starbucks", "Simple Thai",
            "Bambu", "Musashi's", "Boiling Point"};
    private static final String NOT_CONSUMER = "Richard Pan";
    private static final String NOT_RESTAURANT = "Krusty Krab";
    private static final int MAX_RATING = 6;
    private Random r = new Random();

    @Test
    public void testReviewsConstructor() {
        Reviews reviews = new Reviews();
        assertTrue(reviews.getUsersReviews(NOT_CONSUMER) == null);
        assertTrue(reviews.getRestaurantsReviews(NOT_RESTAURANT) == null);
        assertTrue(reviews.getReviewsByMostRecent().isEmpty());
        assertEquals(reviews.getRestaurantAvgRating(NOT_RESTAURANT), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testAddReview() {
        Reviews reviews = new Reviews();
        for (String consumer : CONSUMER_NAMES) {
            for (String restaurant : RESTAURANT_NAMES) {
                int rating = r.nextInt(MAX_RATING);
                Review review = new Review(consumer, restaurant, rating);
                reviews.addReview(review);
            }
        }
        for (String consumerName : CONSUMER_NAMES) {
            assertEquals(reviews.getUsersReviews(consumerName).size(), RESTAURANT_NAMES.length);
        }
        for (String restaurantName : RESTAURANT_NAMES) {
            assertEquals(reviews.getRestaurantsReviews(restaurantName).size(), CONSUMER_NAMES.length);
        }
        assertEquals(reviews.getReviewsByMostRecent().size(), RESTAURANT_NAMES.length * CONSUMER_NAMES.length);
        reviews.addReview(null);
    }

    public void testGetRestaurantAvgRating() {
        Reviews reviews = new Reviews();
        double[] ratings = new double[RESTAURANT_NAMES.length];
        for (int i = 0; i < RESTAURANT_NAMES.length; i++) {
            String restaurant = RESTAURANT_NAMES[i];
            ratings[i] = 0.0;
            for (String consumer : CONSUMER_NAMES) {
                int rating = r.nextInt(MAX_RATING);
                ratings[i] += rating;
                Review review = new Review(consumer, restaurant, rating);
                reviews.addReview(review);
            }
        }
        for (int i = 0; i < RESTAURANT_NAMES.length; i++) {
            String restaurant = RESTAURANT_NAMES[i];
            double rating = ratings[i] / (double) reviews.getRestaurantsReviews(restaurant).size();
            assertEquals(reviews.getRestaurantAvgRating(restaurant), rating);
        }
        assertEquals(reviews.getRestaurantsReviews(null), null);
    }
}
