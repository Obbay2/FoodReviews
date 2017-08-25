package com.cdk.food.foodreviews;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by martint on 7/18/17.
 */

public class dbUtility {

    public static JSONObject makeHttpRequest(String url, String method, ContentValues params) {
        // Making HTTP request
        String json = null;
        try {
            final OkHttpClient client = new OkHttpClient();
            Request request = null;
            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                final MediaType JSON
                        = MediaType.parse("application/json; charset=utf-8");
                final RequestBody body = RequestBody.create(JSON, params.toString());
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            } else if (method.equals("GET")) {
                // request method is GET
                request = new Request.Builder()
                        .url(url)
                        .build();
            }
            final Response response = client.newCall(request).execute();
            json = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jObj = null;
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e ){
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }
}
