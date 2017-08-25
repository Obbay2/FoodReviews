package com.cdk.food.foodreviews;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;

import structures.Restaurant;
import structures.RestaurantList;
import structures.Reviews;

public class RestaurantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements java.io.Serializable {

    public RestaurantList items;
    public Reviews reviews;

    /** References to the views for each data item **/
    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleView;
        public ImageView imageView;
        public RatingBar ratingView;
        public View view;
        public Restaurant restaurant;

        public AsyncTask<?, ?, ?> imageTask;

        public ReviewViewHolder(View v) {
            super(v);

            view = v;
            titleView = (TextView) v.findViewById(R.id.textView);
            ratingView = (RatingBar) v.findViewById(R.id.rating);
            imageView = (ImageView) v.findViewById(R.id.imageView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(v.getContext(), RestaurantDetailActivity.class);
            intent.putExtra("RestaurantName", restaurant.getRestaurantName());
            intent.putExtra("img_url", restaurant.getPhotoUrl());
            intent.putExtra("restaurantAddress", restaurant.getPhysicalAddress());

            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", restaurant.getLatitude());
            bundle.putDouble("longitude", restaurant.getLongitude());
            bundle.putFloat("rating", (float) reviews.getRestaurantAvgRating(restaurant.getRestaurantName()));

            intent.putExtras(bundle);

            v.getContext().startActivity(intent);
        }
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
            if(!isCancelled()) {
                bmImage.setImageBitmap(result);
            }
        }
    }

    /** Constructor **/
    public RestaurantAdapter(RestaurantList items, Reviews reviews) {
        this.items = items;
        this.reviews = reviews;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card_view, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        ReviewViewHolder vh = (ReviewViewHolder) holder;
        vh.imageView.setImageResource(0);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Restaurant item = items.get(position);
        ReviewViewHolder vh = (ReviewViewHolder) holder;
        vh.restaurant = item;

        vh.titleView.setText(item.getRestaurantName());

        vh.ratingView.setRating((float) reviews.getRestaurantAvgRating(item.getRestaurantName()));

        if(vh.imageTask != null) {
            vh.imageTask.cancel(true);
        }

        vh.imageTask = new DownloadImageTask(vh.imageView).execute(item.getPhotoUrl());
    }

}
