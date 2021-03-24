package com.example.journal;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private String imageUrl;
    private Timestamp timeAdded;
    private String userId;
    private String username;

    public Journal() { } // must for fireStore to work

    public Journal(String title, String thought, String imageUrl, Timestamp timeAdded, String userId, String username) {
        this.title = title;
        this.thought = thought;
        this.imageUrl = imageUrl;
        this.timeAdded = timeAdded;
        this.userId = userId;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
