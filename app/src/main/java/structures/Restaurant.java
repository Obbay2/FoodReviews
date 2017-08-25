package structures;

import android.location.Location;

public class Restaurant implements java.io.Serializable {
    /* name of the restaurant */
    private String restaurantName = "";
    /* photoReference for this restaurant
        (refer to google API for photo reference definition) */
    private String photoUrl = "";
    /* physical address of this restaurant */
    private String physicalAddress = "";
    /* tags */
    private String tags = "";
    /* location of this restaurant */
    private double latitude = 0.0, longitude = 0.0;
    /* price level of this restaurant out of 5 */
    private int priceLevel = 0;

    /* Creates a new Restaurant */
    public Restaurant(String restaurantName, double latitude, double longitude) {
        if (restaurantName == null) {
            throw new IllegalArgumentException();
        }
        this.restaurantName = restaurantName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /* Edits the image for this restaurant */
    public void editImage(String photoURL) {
        if (photoURL == null)
            throw new IllegalArgumentException();
        this.photoUrl = photoURL;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRestaurantName() { return new String(restaurantName); }

    public String getPhotoUrl() { return new String(photoUrl); }

    public String getPhysicalAddress() {
        return new String(physicalAddress);
    }

    public String getTags() {
        return  new String(tags);
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    /* Compares two Restaurant objects for equality */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Restaurant)) {
            return false;
        }
        Restaurant other = (Restaurant) o;
        return this.restaurantName.equals(other.restaurantName)
                && this.latitude == other.latitude
                && this.longitude == other.longitude;
    }

    /* Returns the hashcode of this restaurant */
    @Override
    public int hashCode() {
        return this.restaurantName.hashCode();
    }

    /* String representation of a restaurant */
    public String toString() {
        return "restaurant name: " + restaurantName +
                "\nlatitude: " + latitude +
                "\nlongitude: " + longitude +
                "\nphotoURL: " + photoUrl +
                "\nphysical address: " + physicalAddress +
                "\nprice level: " + priceLevel;
    }
}