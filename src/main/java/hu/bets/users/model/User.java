package hu.bets.users.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("id")
    private String userId;
    @JsonProperty("pictureUrl")
    private String profilePictureUrl;
    @JsonProperty("name")
    private String name;

    public User(String userId, String profilePictureUrl, String name) {
        this.userId = userId;
        this.profilePictureUrl = profilePictureUrl;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
