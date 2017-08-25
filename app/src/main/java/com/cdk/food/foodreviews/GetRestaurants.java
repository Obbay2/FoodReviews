package com.cdk.food.foodreviews;

import android.icu.util.BuddhistCalendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.cdk.food.foodreviews.service.RestaurantParams;
import com.cdk.food.foodreviews.service.ReviewService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import structures.Restaurant;

public class GetRestaurants extends AsyncTask<RestaurantParams, Integer, Set<Restaurant>> {
    private static final String API_KEY = "AIzaSyAuA68XyNCd3C11_6HLSUao7-Ks42jUvdE";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String PHOTO_URI = "https://maps.googleapis.com/maps/api/place/photo?";
    private static final int METERS_FROM_CDK = 500;
    private static final String TYPE = "restaurant";
    private static final String OPEN_NOW = "opennow";
    private static final double[] LOCATIONS_LAT = {47.597327, 45.504361, 31.759144, 39.160760};
    private static final double[] LOCATIONS_LNG = {-122.328626, -122.680131, -106.485508, -84.455489};
    private static final List<String> LOCATIONS = Arrays.asList("Seattle", "Portland", "Austin", "Cincinnati");

    private GoogleMap map;
    private RestaurantParams params;
    private static List<Restaurant> cdkRestaurants;
    private static RestaurantAdapter restaurantAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> nathanRestaurants;
    public GetRestaurants(List<Restaurant> cdkRestaurants, RestaurantAdapter restaurantAdapter) {
        this.cdkRestaurants = cdkRestaurants;
        this.restaurantAdapter = restaurantAdapter;
    }

    public GetRestaurants(GoogleMap map) {
        this.map = map;
    }

    public GetRestaurants(List<String> nathanRestaurants, ArrayAdapter<String> arrayAdapter) {
        this.nathanRestaurants = nathanRestaurants;
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    protected Set<Restaurant> doInBackground(RestaurantParams... params) {
        Set<Restaurant> restaurants = new HashSet<Restaurant>();
        this.params = params[0];
        for (String cdkLocation : params[0].cdkLocation) {
            int index = LOCATIONS.indexOf(cdkLocation);
            if (index == -1) {
                continue;
            }
            double lat = LOCATIONS_LAT[index];
            double lng = LOCATIONS_LNG[index];
            String location = "location=" + lat + "," + lng;
            String radius = "radius=" + METERS_FROM_CDK;
            String type = "type=" + TYPE;
            String key = "key=" + API_KEY;
            try {
                URL url = new URL(BASE_URL + location + "&" + radius + "&" + OPEN_NOW + "&" + type + "&" + key);
                InputStream stream = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                lat = 0.0;
                lng = 0.0;
                String restaurantName = null, imageURL = null, vicinity = null;
                int priceLevel = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim().replace(",", "");
                    if (line.startsWith("\"lat\"") && lat == 0.0) {
                        lat = Double.valueOf(line.substring(line.indexOf(":") + 1, line.length()));
                    } else if (line.startsWith("\"lng\"") && lng == 0.0) {
                        lng = Double.valueOf(line.substring(line.indexOf(":") + 1, line.length()));
                    } else if (line.startsWith("\"name\"")) {
                        restaurantName = line.substring(line.indexOf(":") + 1, line.length()).trim().replace("\"", "");
                    } else if (line.startsWith("\"photo_reference\"")) {
                        imageURL = line.substring(line.indexOf(":") + 1, line.length()).trim().replaceAll("\"", "");
                        imageURL = PHOTO_URI + "maxwidth=400&photoreference=" + imageURL + "&key=" + API_KEY;
                    } else if (line.startsWith("\"price_level\"")) {
                        priceLevel = Integer.valueOf(line.substring(line.indexOf(":") + 1, line.length()).trim());
                    } else if (line.startsWith("\"vicinity\"")) {
                        vicinity = line.substring(line.indexOf(":") + 1, line.length()).trim().replace("\"", "");
                    }
                    if (lat != 0.0 && lng != 0.0 && restaurantName != null && imageURL != null
                            && priceLevel != 0 && vicinity != null) {
                        Restaurant restaurant = new Restaurant(restaurantName, lat, lng);
                        restaurant.setPhysicalAddress(vicinity);
                        restaurant.setPriceLevel(priceLevel);
                        if (imageURL != null) {
                            restaurant.editImage(imageURL);
                        }
                        restaurants.add(restaurant);
                        lat = 0.0;
                        lng = 0.0;
                        restaurantName = null;
                        imageURL = null;
                        vicinity = null;
                    }
                }

                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return restaurants;
    }

    @Override
    protected void onPostExecute(Set<Restaurant> restaurants) {


        if(params.request.equalsIgnoreCase("load")) {
            cdkRestaurants.addAll(restaurants);
            restaurantAdapter.notifyDataSetChanged();

            // calculate each restaurant's percentile compared to others.
            Map<String, Double> restaurantsToRatings = new HashMap<>();
            List<Double> ratings = new ArrayList<>();
            for(Restaurant restaurant : restaurants) {
                double rating = restaurantAdapter.reviews.getRestaurantAvgRating(restaurant.getRestaurantName());

                ratings.add(rating);
                restaurantsToRatings.put(restaurant.getRestaurantName(), rating);
            }
            Collections.sort(ratings);
            for (Restaurant restaurant: restaurants) {
                String restaurantName = restaurant.getRestaurantName();
                ReviewService.setPercentile(restaurantName, (float) (ratings.indexOf(restaurantsToRatings.get(restaurantName)) * 1.0 / ratings.size()));
            }
            System.out.println("");
        } else if(params.request.equalsIgnoreCase("updateLoad")) {
            LatLng cdk = new LatLng(47.5973163, -122.3285244);
            map.addMarker(new MarkerOptions().position(cdk).title("CDK Global Seattle").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cdk, 15));

            for (Restaurant restaurant : restaurants) {
                System.out.println("restaurant " + restaurant.toString());
                LatLng location = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
                map.addMarker(new MarkerOptions().position(location).title(restaurant.getRestaurantName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        } else if(params.request.equalsIgnoreCase("restaurants")) {
            for(Restaurant restaurant : restaurants) {
                nathanRestaurants.add(restaurant.getRestaurantName());
            }
            arrayAdapter.notifyDataSetChanged();
        } else {
            System.out.println("wtf fix your params request for getRestaurants");
        }
    }
}
