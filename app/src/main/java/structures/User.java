package structures;

public class User {
    /* name of this user */
    private String username = "";
    /* file path of the user's profile picture */
    private String dpFilePath = "";

    /* Creates a new user */
    public User(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        this.username = username;
    }

    /* Edits the username for this user */
    public void editUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        this.username = username;
    }

    /* Changes the profile picture for this user */
    public void editProfilePicture(String dpFilePath) {
        if (dpFilePath == null) {
            throw new IllegalArgumentException();
        }
        this.dpFilePath = dpFilePath;
    }

    public String getUsername() {
        return new String(username);
    }

    public String getDpFilePath() {
        return new String(dpFilePath);
    }
}