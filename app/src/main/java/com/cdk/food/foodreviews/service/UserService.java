package com.cdk.food.foodreviews.service;

import com.cdk.food.foodreviews.RequestParams;
import com.cdk.food.foodreviews.TristansFireBase;

import structures.User;

/**
 * Created by yuq on 7/19/17.
 *
 */

public class UserService {
    public static void addUser(User user) {
        RequestParams params = new RequestParams("adduser", user);
        new TristansFireBase().execute(params);
    }
}
