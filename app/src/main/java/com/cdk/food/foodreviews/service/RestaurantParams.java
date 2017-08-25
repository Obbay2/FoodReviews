package com.cdk.food.foodreviews.service;

import com.cdk.food.foodreviews.RequestParams;

/**
 * Created by martint on 7/20/17.
 */

public class RestaurantParams {

    public String request;
    public String[] cdkLocation;

    public RestaurantParams(String request, String[] cdkLocation) {
        this.request = request;
        this.cdkLocation = cdkLocation;
    }
}
