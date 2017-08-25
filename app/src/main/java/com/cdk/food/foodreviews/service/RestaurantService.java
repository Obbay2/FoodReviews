package com.cdk.food.foodreviews.service;

import com.cdk.food.foodreviews.RequestParams;
import com.cdk.food.foodreviews.TristansFireBase;

import structures.Restaurant;

public class RestaurantService {

    public static void addRestaurant(Restaurant restaurant) {

        RequestParams params = new RequestParams("addRestaurant", restaurant, "restaurants");
        new TristansFireBase().execute(params);

    }

    public static void updateRating(Restaurant restaurant, int rating) {
        RequestParams params = new RequestParams("updaterestaurantrating", restaurant, "restaurants", rating);
        new TristansFireBase().execute(params);
    }

}
