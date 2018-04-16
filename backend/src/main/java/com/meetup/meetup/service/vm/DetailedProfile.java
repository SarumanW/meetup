package com.meetup.meetup.service.vm;

public class DetailedProfile {
    private String title;
    private String first;
    private String last;
    private String email;
    private String username;

    public DetailedProfile(Profile profile) {
        title = profile.getTitle();
        first = profile.getFirst();
        last = profile.getLast();
        email = profile.getEmail();
        username = profile.getUsername();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
