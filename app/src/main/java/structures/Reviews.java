package structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Reviews implements java.io.Serializable {
    /* an ordered set of reviews by most recent */
    private static Set<Review> reviewsByMostRecent;
    /* a map from users to reviews that they have written */
    private static Map<String, Set<Review>> userToReviews;
    /* a map from restaurants to reviews that they've got */
    private static Map<String, Set<Review>> restaurantToReviews;
    /* a map from users defined by usernames to a user */
    private static Map<String, User> userNameToUser;

    public Reviews() {
        this.userToReviews = new HashMap<String, Set<Review>>();
        this.restaurantToReviews = new HashMap<String, Set<Review>>();
        this.reviewsByMostRecent = new TreeSet<>();
        this.userNameToUser = new HashMap<>();
    }

    public Reviews(List<Review> reviews) {
        Collections.sort(reviews);
        for (int i = 0; i < Math.min(20, reviews.size()); i++) {
            addReview(reviews.get(i));
        }
    }

    /* Adds a new review to this review container. */
    public boolean addReview(Review review) {
        if (reviewsByMostRecent.add(review)) {
            if (!userToReviews.containsKey(review.getConsumerName())) {
                userToReviews.put(review.getConsumerName(), new TreeSet<Review>());
            }
            userToReviews.get(review.getConsumerName()).add(review);
            if (!restaurantToReviews.containsKey(review.getRestaurantName())) {
                restaurantToReviews.put(review.getRestaurantName(), new TreeSet<Review>());
            }
            restaurantToReviews.get(review.getRestaurantName()).add(review);
            return true;
        }
        return false;
    }

    /* Adds a new user who can write a review */
    public void addUser(User user) {
        userNameToUser.put(user.getUsername(), user);
    }

    /* Returns the current number of reviews in this container */
    public int size() {
        return reviewsByMostRecent.size();
    }

    /* Returns the reviews associated with a specific username. Returning NULL, if
     * user defined by the username does not exist.  */
    public Set<Review> getUsersReviews(String username) {
        return userToReviews.get(username);
    }

    /* Returns the user object for this username. Returning NULL, if there is no
    * user with the given username. */
    public User getUserByUsername(String username) { return userNameToUser.get(username); }

    /* Returns the reviews associated with a specific restaurant. Returning NULL, if
    * the restaurant defined by the restaurant name does not exist. */
    public Set<Review> getRestaurantsReviews(String restaurantName) {
        return restaurantToReviews.get(restaurantName);
    }

    /*  Returns a sorted set of all of the reviews, list by most recent. Returning
     * an empty set if there no reviews currently. */
    public Set<Review> getReviewsByMostRecent() {
        return reviewsByMostRecent;
    }

    /* Returns the avg rating of a restuarant. Returning 0, if there are no
    reviews for this restaurant. */
    public double getRestaurantAvgRating(String restaurantName) {
        double avgRating = 0.0;
        Set<Review> reviews = restaurantToReviews.get(restaurantName);
        if(reviews != null) {
            for (Review review : reviews) {
                avgRating += (double) review.getRating();
            }
            return avgRating / reviews.size();
        }
        return 0;
    }
}