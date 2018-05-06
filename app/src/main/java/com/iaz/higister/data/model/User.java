package com.iaz.higister.data.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alksander on 17/03/2018.
 */

public class User {

    private String name;
    private int followersNumber;
    private ArrayList<User> followers;
    private int listsCreatedNumber;
    private ArrayList<UserList> listsCreated;
    private int listsFavouritedNumber;
//    public ArrayList<UserList> listsFavorited;
private String description;
    private int age;
    private ArrayList<String> interests = new ArrayList<>();
    private String profilePictureUri;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowersNumber() {
        return followersNumber;
    }

    public void setFollowersNumber(int followersNumber) {
        this.followersNumber = followersNumber;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<User> followers) {
        this.followers = followers;
    }

    public int getListsCreatedNumber() {
        return listsCreatedNumber;
    }

    public void setListsCreatedNumber(int listsCreatedNumber) {
        this.listsCreatedNumber = listsCreatedNumber;
    }

    public ArrayList<UserList> getListsCreated() {
        return listsCreated;
    }

    public void setListsCreated(ArrayList<UserList> listsCreated) {
        this.listsCreated = listsCreated;
    }

    public int getListsFavouritedNumber() {
        return listsFavouritedNumber;
    }

    public void setListsFavouritedNumber(int listsFavouritedNumber) {
        this.listsFavouritedNumber = listsFavouritedNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }
}
