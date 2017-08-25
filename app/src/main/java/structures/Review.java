package structures;

import java.text.DateFormat;

public class Review implements Comparable<Review> {
    /* name of the user who made this review */
    private String consumerName = "";
    /* name of the restaurant this user went to */
    private String restaurantName = "";
    /* text of the review */
    private String reviewBody = "";
    /* file path to the image */
    private String foodImageUrl = "";
    /* rating of the food by the consumer out of 5 */
    private int rating;
    /* time when this review was made */
    private long timeStamp;

    private static final boolean DEBUG_STATUS = true;

    /* Constructor for writing new reviews */
    public Review(String consumerName, String restaurantName, int rating) {
        if (consumerName == null || restaurantName == null || rating < 0 || rating > 5) {
            throw new IllegalArgumentException();
        }
        this.consumerName = consumerName;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.reviewBody = "";
        this.timeStamp = System.currentTimeMillis();
        checkRep(DEBUG_STATUS);
    }

    /* Constructor for loading pre-existing reviews */
    public Review(String consumerName, String restaurantName, int rating, Long timeStamp) {
        this(consumerName, restaurantName, rating);
        if (timeStamp == null) {
            throw new IllegalArgumentException();
        }
        this.timeStamp = timeStamp;
        checkRep(DEBUG_STATUS);
    }

    /* Adds a body of text for this review */
    public void addBody(String reviewBody) {
        if (reviewBody == null) {
            throw new IllegalArgumentException();
        }
        this.reviewBody = reviewBody;
        checkRep(DEBUG_STATUS);
    }

    /* Adds a picture to this review */
    public void addPicture(String imageFilePath) {
        if (imageFilePath == null) {
            throw new IllegalArgumentException();
        }
        this.foodImageUrl = imageFilePath;
        checkRep(DEBUG_STATUS);
    }

    public String getConsumerName() {
        return new String(consumerName);
    }

    public String getRestaurantName() {
        return new String(restaurantName);
    }

    public int getRating() {
        return rating;
    }

    public String getReviewBody() { return new String(reviewBody); }

    public String getFoodImageUrl() { return new String(foodImageUrl); }

    public Long getTimeStampMilli() {
        return timeStamp;
    }

    public String getTimeStampDate() {
        return DateFormat.getDateTimeInstance().format(timeStamp);
    }

    /* Compares this Review to a timeStamp to see which is more recent */
    public int compareTo(Long otherTimeStamp) {
        if(this.timeStamp > otherTimeStamp) {
            return -1;
        } else if(this.timeStamp < otherTimeStamp) {
            return 1;
        } else {
            return 0;
        }
    }

    /* Compares two Review objects */
    @Override
    public int compareTo(Review r) {
        if(this.timeStamp != (r.timeStamp)) {
            return compareTo(r.timeStamp);
        } else if (!this.consumerName.equals(r.getConsumerName())) {
            return this.getConsumerName().compareTo(r.getConsumerName());
        }
        return this.restaurantName.compareTo(r.restaurantName);
    }

    /* Return the hashCode of this Review object */
    @Override
    public int hashCode() {
        return (int) this.timeStamp % 31;
    }

    /* Compares to Review objects for equality */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Review)) {
            return false;
        }
        Review o = (Review) other;
        return this.timeStamp == (o.timeStamp)
                && this.consumerName.equals(o.getConsumerName())
                && this.restaurantName.equals(o.restaurantName)
                && this.getRating() == o.getRating();
    }

    /* Returns the string representation of this review */
    public String toString() {
        return consumerName + " rated " + restaurantName +
                " " + rating + " out of 5 stars.";
    }

    private void checkRep(boolean debugStatus) {
        assert consumerName != null : "consumer name is set to null";
        assert restaurantName != null : "restaurant name is set to null";
        assert reviewBody != null : "review body is set to null";
        assert foodImageUrl != null : "food image URL is set to null";
        assert timeStamp != 0 : "time stamp has not been set up properly for this review";
        assert rating >= 0 && rating <= 5 : "ratings for a review is out of bounds";
    }
}
