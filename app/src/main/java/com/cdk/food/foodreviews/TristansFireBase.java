package com.cdk.food.foodreviews;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import structures.JoinedUserAndReview;
import structures.Restaurant;
import structures.Review;
import structures.Reviews;
import structures.User;

public class TristansFireBase extends AsyncTask<RequestParams, Void, JSONObject> {
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");
    public Reviews firebaseReviews;
    public SocialAdapter socialAdapter;
    public RestaurantAdapter restaurantAdapter;
    public List<JoinedUserAndReview> joinedUserAndReviews = new ArrayList<>();
    public RequestParams requestParams;

    public TristansFireBase(List<JoinedUserAndReview> joinedUserAndReviews, SocialAdapter socialAdapter, RestaurantAdapter restaurantAdapter) {
        this.firebaseReviews = firebaseReviews;
        this.socialAdapter = socialAdapter;
        this.restaurantAdapter = restaurantAdapter;
        this.joinedUserAndReviews = joinedUserAndReviews;

    }

    public TristansFireBase() {

    }

    String post(String url, String json, OkHttpClient client) throws IOException {
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected JSONObject doInBackground(RequestParams... param) {

        requestParams = param[0];
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = dataBase.getReference("interactions");
        DatabaseReference dbParamRef = dataBase.getReference("paramaters");
        dbParamRef.setValue(param[0]);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("");
            }
        });

        String url = "https://mldance-1500313678788.firebaseio.com/.json";
        JSONObject testJSON = dbUtility.makeHttpRequest(url, "GET", null);
        int interactions = 0;

        try {
            interactions = Integer.parseInt(testJSON.get("interactions").toString()) + 1;
            dbRef.setValue(interactions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (param[0].request.equalsIgnoreCase("add")) {
            System.out.println("added");
            //        String urlForOfferId = "http://services.dit-ord.cobaltgroup.com/offer/rest/v1.0/offers/" + 25479430;
            addReview(dataBase, "review" + interactions, param[0].review);
            return null;
        } else if (param[0].request.equalsIgnoreCase("readreviews")) {
            url = param[0].path;
            JSONObject reviews = dbUtility.makeHttpRequest(url, "GET", null);
            return reviews;
        } else if (param[0].request.equalsIgnoreCase("adduser")) {
            addUser(dataBase, param[0].user);
            return null;
        } else if (param[0].request.equalsIgnoreCase("readusers")) {
            url = param[0].path;
            JSONObject users = dbUtility.makeHttpRequest(url, "GET", null);
            return users;
        } else if (param[0].request.equalsIgnoreCase("mergeusersreviews")) {

            JSONObject holder = new JSONObject();
            try {
                holder.accumulate("users", dbUtility.makeHttpRequest("https://mldance-1500313678788.firebaseio.com/users.json", "GET", null));
                holder.accumulate("reviews", dbUtility.makeHttpRequest("https://mldance-1500313678788.firebaseio.com/reviews.json", "GET", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return holder;
        } else if (param[0].request.equalsIgnoreCase("addrestaurant")) {
            addRestaurant(dataBase, param[0].restaurant);
            return null;
        } else if (param[0].request.equalsIgnoreCase("readrestaurants")) {
            url = param[0].path;
            JSONObject restaurants = dbUtility.makeHttpRequest(url, "GET", null);
            return restaurants;
        } else if (param[0].request.equalsIgnoreCase("updaterestaurantrating")) {
            url = param[0].path;
            int newRating = param[0].rating;
            JSONObject restaurants = dbUtility.makeHttpRequest(url, "GET", null);
            String restaurantName = param[0].restaurant.getRestaurantName();
            try {
                JSONObject thisRestaurant = restaurants.getJSONObject(restaurantName);
                int ratingCount = thisRestaurant.getInt("ratingCount");
                double currentRating = thisRestaurant.getDouble("overallRating");

                updateRestaurantRating(dataBase, param[0].restaurant, newRating, ratingCount, currentRating);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            System.out.println("wtf are you doing with your params dude, try again fool " + param[0].request);
            return null;
        }
    }

    protected void onPostExecute(JSONObject values) {
        if (requestParams.request.equalsIgnoreCase("readreviews")) {
            readReviews(values);
        } else if (requestParams.request.equalsIgnoreCase("add")) {

        } else if (requestParams.request.equalsIgnoreCase("adduser")) {

        } else if (requestParams.request.equalsIgnoreCase("mergeusersreviews")) {
            parseUsers(values);
        } else if (requestParams.request.equalsIgnoreCase("addrestaurant")) {

        } else if (requestParams.request.equalsIgnoreCase("readrestaurants")) {

        } else if (requestParams.request.equalsIgnoreCase("updaterestaurantrating")) {

        } else {
            System.out.println("wtf was your request param? try again fool".toUpperCase());
        }
        // call Nathan's screen changer
    }

    public void parseUsers(JSONObject values) {
        System.out.println("After: " + (socialAdapter == null));

        System.out.println("merging users and reviews " + values);

        try {
            JSONObject users = values.getJSONObject("users");
            JSONObject mergeReviews = values.getJSONObject("reviews");

            Iterator<?> userKeys = users.keys();
            Iterator<?> reviewsKeys = mergeReviews.keys();

            ArrayList<Review> angelReviews = new ArrayList<Review>();

            while (reviewsKeys.hasNext()) {
                String reviewKey = (String) reviewsKeys.next();
                JSONObject reviewValues = mergeReviews.getJSONObject(reviewKey);
                Review tempReview = new Review(reviewValues.getString("consumerName"),
                        reviewValues.getString("restaurantName"), reviewValues.getInt("rating"),
                        reviewValues.getLong("timeStamp"));
                tempReview.addBody(reviewValues.getString("body"));
                tempReview.addPicture(reviewValues.getString("img_path"));
                angelReviews.add(tempReview);
            }

            Reviews tempReviews = new Reviews(angelReviews);

            while (userKeys.hasNext()) {
                String userKey = (String) userKeys.next();
                JSONObject userValues = users.getJSONObject(userKey);
                User tempUser = new User(userValues.getString("username"));
                tempUser.editProfilePicture(userValues.getString("pathToImage"));
                System.out.println(tempUser.getUsername());
                tempReviews.addUser(tempUser);
            }

            for (Review r : tempReviews.getReviewsByMostRecent()) {
                JoinedUserAndReview temp = new JoinedUserAndReview(tempReviews.getUserByUsername(r.getConsumerName()), r);
                joinedUserAndReviews.add(temp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        socialAdapter.notifyDataSetChanged();
    }

    public void readReviews(JSONObject values) {
        try {
            System.out.println("hello " + requestParams.request + " " + requestParams.path);
            ArrayList<Review> reviews = new ArrayList<Review>();
            Iterator<?> keys = values.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                JSONObject value = values.getJSONObject(key);
                Review review = new Review(value.getString("consumerName"),
                        value.getString("restaurantName"), value.getInt("rating"),
                        value.getLong("timeStamp"));
                reviews.add(review);
                System.out.println(review.toString());
                firebaseReviews.addReview(review);
            }
//            socialAdapter.reviewList.addAll(firebaseReviews.getReviewsByMostRecent());
//            socialAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addReview(FirebaseDatabase database, String reviewID, Review review) {
        DatabaseReference reviewReference = database.getReference("reviews");
        System.out.println("adding rating");
        reviewReference.child(reviewID).child("rating").setValue(review.getRating());
        System.out.println("adding body");
        reviewReference.child(reviewID).child("body").setValue(review.getReviewBody());
        System.out.println("adding image path");
        reviewReference.child(reviewID).child("img_path").setValue(review.getFoodImageUrl());
        System.out.println("adding consumername");
        reviewReference.child(reviewID).child("consumerName").setValue(review.getConsumerName());
        System.out.println("adding restaurant name");
        reviewReference.child(reviewID).child("restaurantName").setValue(review.getRestaurantName());
        System.out.println("adding timeStamp");
        reviewReference.child(reviewID).child("timeStamp").setValue(review.getTimeStampMilli());
    }

    public void addUser(FirebaseDatabase database, User user) {
        DatabaseReference userRef = database.getReference("users");
        userRef.child(user.getUsername()).child("pathToImage").setValue(user.getDpFilePath());
        userRef.child(user.getUsername()).child("username").setValue(user.getUsername());
    }

    public void addRestaurant(FirebaseDatabase database, Restaurant restaurant) {
        DatabaseReference restaurantRef = database.getReference("restaurants");
        restaurantRef.child(restaurant.getRestaurantName()).child("restaurantName").
                setValue(restaurant.getRestaurantName());
        restaurantRef.child(restaurant.getRestaurantName()).child("latitude").
                setValue(restaurant.getLatitude());
        restaurantRef.child(restaurant.getRestaurantName()).child("longitude").
                setValue(restaurant.getLongitude());
        restaurantRef.child(restaurant.getRestaurantName()).child("overallRating").setValue(0);
        restaurantRef.child(restaurant.getRestaurantName()).child("ratingCount").setValue(0);

    }

    public void updateRestaurantRating(FirebaseDatabase database, Restaurant restaurant,
                                       int newRating, int ratingCount, double currentRating) {
        DatabaseReference restaurantRef = database.getReference("restaurants");

        restaurantRef.child(restaurant.getRestaurantName()).child("overallRating").setValue(
                (currentRating * ratingCount + newRating) * 1.0 / (ratingCount + 1));

        restaurantRef.child(restaurant.getRestaurantName()).child("ratingCount").setValue(ratingCount + 1);
    }
}