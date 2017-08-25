package com.cdk.food.foodreviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import FireBase.FireBaseInterface;
import structures.JoinedUserAndReview;
import structures.RestaurantList;
import structures.Review;
import structures.Reviews;

public class MainActivity extends FragmentActivity {
    static final int NUM_ITEMS = 3;

    MyAdapter mAdapter;
    SocialAdapter socialAdapter;
    RestaurantAdapter restaurantAdapter;
    Reviews reviews;
    List<JoinedUserAndReview> joinedUserAndReviewList;
    RestaurantList restaurantList;

    ViewPager mPager;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        String[] names = {"Jin Xie", "Tristan Martin", "Angel Lee", "David Shen", "Nathan Wreggit", "Andrew Yu", "Osman Shoukry", "Richard Pan", "Madhu Nayak"};
        String[] body = {"nice!", "could have been better but not bad", "I like food", "it was a bit of a walk",
            "I would love for there to be more options", "I only eat burgers", "not as clean as I would have liked"};

        String[] reviewPictures = {"http://cache.boston.com/bonzai-fba/Original_Photo/2010/12/14/kitchen3.jpg__1292367032_7968.jpg",
        "http://cache.boston.com/bonzai-fba/Original_Photo/2010/12/14/Wiggs_almanovehingham1-_foo__1292367289_3691.jpg",
                "http://cache.boston.com/bonzai-fba/Original_Photo/2010/12/14/Boghosian_Ariana_LIV__1292367498_5012.jpg",
                "http://cache.boston.com/bonzai-fba/Original_Photo/2010/12/14/mcdonald_07dining2_fd__1292366816_7384.jpg",
                "https://s-media-cache-ak0.pinimg.com/originals/fc/9e/79/fc9e7997a005df4103e14226dec5b616.jpg",
                "http://blog.grandluxuryhotels.com/wp-content/uploads/2016/03/Restaurant-Le-Gabriel-Pigeon-de-Racan-Marine-au-cacao-Tagliatelle-de-sarrasin-bio.jpg",
                "http://blog.grandluxuryhotels.com/wp-content/uploads/2016/03/Restaurant-Le-Gabriel-Homard-bleu-Carbonara-oignon-chorizo-Chips-des-Cevennes.jpg",
                "http://blog.grandluxuryhotels.com/wp-content/uploads/2016/03/Restaurant-Le-Gabriel-Foie-gras-de-canard-Pomme-vert-au-verjus-Gelee-de-yuzu.jpg"
        };

        String[] restaurantNames = {"Elysian Fields", "Jade Garden Restaurant", "Harbor City Restaurant",
        "Caffe Vita", "Crawfish King", "Shanghai Garden", "Pacific Cafe - Hong Kong Kitchen", "Il Terrazzo Carmine",
        "The Lodge Sports Grille", "King Street Bar & Oven", "Kau Kau Restaurant", "Samurai Noodle",
        "Pho Ba", "Dim Sum King", "Ho Ho Seafood Restaurant", "World Pizza", "House of Hong Restaurant",
        "Mike's Noodle House", "Gyro House", "Liana Cafe House"};

        Random r = new Random();
        // for(int i = 0; i < 1000; i++) {
            Review testReview = new Review(names[r.nextInt(names.length)],
                    restaurantNames[r.nextInt(restaurantNames.length)], r.nextInt(6));
            testReview.addBody(body[r.nextInt(body.length)]);
            testReview.addPicture(reviewPictures[r.nextInt(reviewPictures.length)]);
            FireBaseInterface.addReview(testReview);
        // }

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ReviewActivity.class);
                startActivity(intent);
            }
        });


        reviews = new Reviews();
        joinedUserAndReviewList = new ArrayList<>();
        restaurantList = new RestaurantList();
        socialAdapter = new SocialAdapter(joinedUserAndReviewList);
        restaurantAdapter = new RestaurantAdapter(restaurantList, reviews);
        FireBaseInterface.getMergedUsersReviews(restaurantAdapter, socialAdapter, joinedUserAndReviewList);
        String[] cdkLocations = {"Seattle"};
        GetRestaurantsInterface.getRestaurants(cdkLocations, restaurantList, restaurantAdapter);

        mAdapter = new MyAdapter(getSupportFragmentManager(), socialAdapter, restaurantAdapter);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        TabLayout layout = (TabLayout) findViewById(R.id.layout);
        layout.setupWithViewPager(mPager);
        layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(Tab tab) { }

            @Override
            public void onTabUnselected(Tab tab) { }
        });

        mPager.setCurrentItem(1);
    }

    public static class MyAdapter extends FragmentPagerAdapter {

        public SocialAdapter socialAdapter;
        public RestaurantAdapter restaurantAdapter;

        public MyAdapter(FragmentManager fm, SocialAdapter socialAdapter, RestaurantAdapter restaurantAdapter) {
            super(fm);
            this.socialAdapter = socialAdapter;
            this.restaurantAdapter = restaurantAdapter;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int index) {

            Bundle bundle = new Bundle();

            switch (index) {
                case 0:
                    MapFragment mapFrag = new MapFragment();
                    bundle.putSerializable("restaurants", restaurantAdapter.items);
                    mapFrag.setArguments(bundle);

                    return mapFrag;
                case 1:
                    RestaurantFragment restaurantFrag = new RestaurantFragment();
                    bundle.putSerializable("adapter", restaurantAdapter);
                    restaurantFrag.setArguments(bundle);
                    return restaurantFrag;
                case 2:
                    SocialFragment socialFrag = new SocialFragment();
                    bundle.putSerializable("adapter", socialAdapter);
                    socialFrag.setArguments(bundle);
                    return socialFrag;
                default:
                    return new RestaurantFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return "Map";
                case 1:
                    // Games fragment activity
                    return "Feed";
                case 2:
                    // Movies fragment activity
                    return "Social";
                default:
                    return "ERROR";
            }
        }
    }
}
