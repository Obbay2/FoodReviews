package com.cdk.food.foodreviews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cdk.food.foodreviews.service.ReviewService;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.InputStream;

public class RestaurantDetailActivity extends FragmentActivity {

    ImageView restaurantImage;
    TextView title, ratingValue, restaurantAddress;
    RatingBar restaurantRating;
    DonutProgress donutProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        Intent intent = getIntent();

        restaurantImage = (ImageView) findViewById(R.id.restaurantImage);
        new DownloadImageTask(restaurantImage).execute(getIntent().getStringExtra("img_url"));

        title = (TextView) findViewById(R.id.title);
        title.setText(intent.getStringExtra("RestaurantName"));

        Bundle bundle = intent.getExtras();

        restaurantRating = (RatingBar) findViewById(R.id.restaurantRating);
        restaurantRating.setRating(bundle.getFloat("rating"));

        ratingValue = (TextView) findViewById(R.id.ratingValue);
        ratingValue.setText(String.format("%.2g", bundle.getFloat("rating"))+"/5.0");

        restaurantAddress = (TextView) findViewById(R.id.restaurantAddress);
        restaurantAddress.setText(intent.getStringExtra("restaurantAddress"));

        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        int percentile = Math.round(100*ReviewService.getPercentile(intent.getStringExtra("RestaurantName")));
        donutProgress.setStartingDegree(-90);
        donutProgress.setText(percentile + "%");
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
