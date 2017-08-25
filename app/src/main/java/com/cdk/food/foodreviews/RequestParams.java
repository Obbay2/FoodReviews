package com.cdk.food.foodreviews;

import structures.Restaurant;
import structures.Review;
import structures.User;

/**
 * Created by martint on 7/18/17.
 */

public class RequestParams {

    public String request;
    public Review review;
    public String read;
    public String path;
    public User user;
    public Restaurant restaurant;
    public int rating;

    public RequestParams(String request, Review review, String path) {
        this.request = request;
        this.review = review;
        this.path = "https://mldance-1500313678788.firebaseio.com/" + path + ".json";
    }

    public RequestParams(String request, String read, String path) {
        this.read = read;
        this.request = request;
        this.path = "https://mldance-1500313678788.firebaseio.com/" + path + ".json";
    }

    public RequestParams(String request, Review review) {
        this.request = request;
        this.review = review;
        this.path = "https://mldance-1500313678788.firebaseio.com/.json";
    }

    public RequestParams(String request, User user, String path) {
        this.request = request;
        this.user = user;
        this.path = "https://mldance-1500313678788.firebaseio.com/" + path + ".json";
    }

    public RequestParams(String request, User user) {
        this.request = request;
        this.user = user;
        this.path = "https://mldance-1500313678788.firebaseio.com/.json";
    }

    public RequestParams(String request, Restaurant restaurant, String path) {
        this.request = request;
        this.restaurant = restaurant;
        this.path = "https://mldance-1500313678788.firebaseio.com/" + path + ".json";
    }

    public RequestParams(String request, Restaurant restaurant) {
        this.request = request;
        this.restaurant = restaurant;
        this.path = "https://mldance-1500313678788.firebaseio.com/.json";
    }

    public RequestParams(String request, Restaurant restaurant, String path, int rating) {
        this.request = request;
        this.restaurant = restaurant;
        this.path = "https://mldance-1500313678788.firebaseio.com/" + path + ".json";
        this.rating = rating;
    }
}
