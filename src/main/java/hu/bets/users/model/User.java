package hu.bets.users.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty("id")
    private String userId;
    @JsonProperty("pictureUrl")
    private String profilePictureUrl;
    @JsonProperty("name")
    private String name;

    @JsonCreator
    public User(@JsonProperty("id") String userId, @JsonProperty("pictureUrl") String profilePictureUrl, @JsonProperty("name") String name) {
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
