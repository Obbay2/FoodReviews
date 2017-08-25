package FireBase;

import com.cdk.food.foodreviews.RequestParams;
import com.cdk.food.foodreviews.RestaurantAdapter;
import com.cdk.food.foodreviews.SocialAdapter;
import com.cdk.food.foodreviews.TristansFireBase;

import java.util.List;

import structures.JoinedUserAndReview;
import structures.Review;
import structures.Reviews;
import structures.User;

/**
 * Created by martint on 7/19/17.
 */

public class FireBaseInterface {
    public static RequestParams params;

    public static void addUser(User user) {
        params = new RequestParams("adduser", user);
        new TristansFireBase().execute(params);
    }

    public static void addReview(Review review) {
        params = new RequestParams("add", review);
        new TristansFireBase().execute(params);
    }

    public static void getMergedUsersReviews(RestaurantAdapter restaurantAdapter, SocialAdapter socialAdapter, List<JoinedUserAndReview> usersAndReviews) {
        params = new RequestParams("mergeusersreviews", "review", "");
        System.out.println("Before: " + (socialAdapter == null));
        new TristansFireBase(usersAndReviews, socialAdapter, restaurantAdapter).execute(params);
    }
}
