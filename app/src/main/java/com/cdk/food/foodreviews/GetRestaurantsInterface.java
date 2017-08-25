package com.cdk.food.foodreviews;

import android.widget.ArrayAdapter;

import com.cdk.food.foodreviews.service.RestaurantParams;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import structures.Restaurant;

public class GetRestaurantsInterface {

    public static void getRestaurants(String[] cdkLocation, List<Restaurant> restaurants, RestaurantAdapter adapter) {
        RestaurantParams params = new RestaurantParams("load", cdkLocation);
        new GetRestaurants(restaurants, adapter).execute(params);
    }

    public static void getRestaurantsUpdateMap(String[] cdkLocation, GoogleMap map) {
        RestaurantParams params = new RestaurantParams("updateLoad", cdkLocation);
        new GetRestaurants(map).execute(params);
    }

    public static void getRestaurants(String[] cdkLocation, List<String> restaurantsNames, ArrayAdapter<String> adapter) {
        RestaurantParams params = new RestaurantParams("restaurants", cdkLocation);
        new GetRestaurants(restaurantsNames, adapter).execute(params);
    }
}
