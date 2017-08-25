package com.cdk.food.foodreviews.service;

import com.cdk.food.foodreviews.GetRestaurantsInterface;
import com.cdk.food.foodreviews.RequestParams;
import com.cdk.food.foodreviews.TristansFireBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import structures.Restaurant;
import structures.Review;


/**
 * Created by yuq on 7/19/17.
 *
 */

public class ReviewService {
    private static Map<String, Float> restaurantPercentile = new HashMap<>();



    public static void addReview(Review newReview) {
        RequestParams params = new RequestParams("add", newReview, "");
        new TristansFireBase().execute(params);

    }

    public static void calculatePercentile(String restaurantName) {
        String[] cdkLocations = new String[1];
        cdkLocations[0] = "Seattle";
        List<Restaurant> restaurants = new ArrayList<>();

//        GetRestaurantsInterface.getRestaurants();
    }

    public static void setPercentile(String restaurantName, float percentile) {
        restaurantPercentile.put(restaurantName, percentile);
    }

    public static float getPercentile(String restaurantName) {
        return restaurantPercentile.get(restaurantName);
    }
}
