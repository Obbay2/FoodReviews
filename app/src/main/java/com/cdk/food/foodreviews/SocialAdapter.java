package com.cdk.food.foodreviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

import structures.JoinedUserAndReview;

public class SocialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements java.io.Serializable {

    public List<JoinedUserAndReview> reviewList;


    /** References to the views for each data item **/
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public ImageView imageView;
        public ImageView featured;
        public RatingBar ratingView;
        public TextView nameView;
        public TextView body;

        public AsyncTask<?, ?, ?> featuredTask;
        public AsyncTask<?, ?, ?> imageTask;

        public ReviewViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.textView);
            nameView = (TextView) v.findViewById(R.id.username);
            body = (TextView) v.findViewById(R.id.body);
            ratingView = (RatingBar) v.findViewById(R.id.rating);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            featured = (ImageView) v.findViewById(R.id.featured);
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
    public SocialAdapter(List<JoinedUserAndReview> reviews) {
        this.reviewList = reviews;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_card_view, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        ReviewViewHolder vh = (ReviewViewHolder) holder;
        vh.imageView.setImageResource(0);
        vh.featured.setImageResource(0);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (reviewList.size() > position && position >= 0) {
            JoinedUserAndReview item = reviewList.get(position);
            System.out.println(item);
            ReviewViewHolder vh = (ReviewViewHolder) holder;

            vh.nameView.setText(item.userName);
            vh.body.setText(item.reviewBody);
            vh.titleView.setText(item.restaurantName);
            vh.ratingView.setRating(item.rating);

            if(vh.featuredTask != null) {
                vh.featuredTask.cancel(true);
            }

            if(vh.imageTask != null) {
                vh.imageTask.cancel(true);
            }

            vh.imageTask = new DownloadImageTask(vh.imageView).execute(item.userPictureUrl);
            vh.featuredTask = new DownloadImageTask(vh.featured).execute(item.foodImageUrl);
        }
    }

}
