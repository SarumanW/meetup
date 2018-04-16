package com.meetup.meetup.service.vm;

public class MinimalProfile {
    private String username;
    private String title;
    private String first;
    private String last;

    public MinimalProfile(Profile profile) {
        title = profile.getTitle();
        first = profile.getFirst();
        last = profile.getLast();
        username = profile.getUsername();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
