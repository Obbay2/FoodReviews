package structures;

public class JoinedUserAndReview implements java.io.Serializable {

    public String userName;
    public String userPictureUrl;
    public String restaurantName = "";
    public String reviewBody = "";
    public String foodImageUrl = "";
    public int rating;
    public long timeStamp;

    public JoinedUserAndReview(User user, Review review) {
        userName = user.getUsername();
        userPictureUrl = user.getDpFilePath();
        restaurantName = review.getRestaurantName();
        reviewBody = review.getReviewBody();
        foodImageUrl = review.getFoodImageUrl();
        rating = review.getRating();
        timeStamp = review.getTimeStampMilli();
    }
}
