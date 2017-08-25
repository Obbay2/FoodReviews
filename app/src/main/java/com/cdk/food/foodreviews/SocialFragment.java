package com.cdk.food.foodreviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import structures.RestaurantList;
import structures.Review;
import structures.Reviews;

public class SocialFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        SocialAdapter adap = (SocialAdapter) bundle.getSerializable("adapter");

        View rootView = inflater.inflate(R.layout.fragment_review_list, container, false);

        RecyclerView list = (RecyclerView) rootView.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        list.setLayoutManager(layoutManager);
        list.setAdapter(adap);

        return rootView;
    }
}
