package com.iaz.higister.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

/**
 * Created by alksander on 17/03/2018.
 */

public class User implements Parcelable {

    @Exclude
    public String uid;

    private String name;
    private int followersNumber;
    //    private ArrayList<User> followers;
    private int listsCreatedNumber;
    //    private ArrayList<UserList> listsCreated;
    private int listsFavouritedNumber;
    //    public ArrayList<UserList> listsFavorited;
    private String bio;
    private int age;
    private ArrayList<String> interests = new ArrayList<>();
    private String profilePictureUri;

    public User() {}

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

//    public ArrayList<User> getFollowers() {
//        return followers;
//    }
//
//    public void setFollowers(ArrayList<User> followers) {
//        this.followers = followers;
//    }

    public int getListsCreatedNumber() {
        return listsCreatedNumber;
    }

    public void setListsCreatedNumber(int listsCreatedNumber) {
        this.listsCreatedNumber = listsCreatedNumber;
    }

//    public ArrayList<UserList> getListsCreated() {
//        return listsCreated;
//    }
//
//    public void setListsCreated(ArrayList<UserList> listsCreated) {
//        this.listsCreated = listsCreated;
//    }

    public int getListsFavouritedNumber() {
        return listsFavouritedNumber;
    }

    public void setListsFavouritedNumber(int listsFavouritedNumber) {
        this.listsFavouritedNumber = listsFavouritedNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    protected User(Parcel in) {
        uid = in.readString();
        setName(in.readString());
        setFollowersNumber(in.readInt());
        setListsCreatedNumber(in.readInt());
        setListsFavouritedNumber(in.readInt());
        setBio(in.readString());
        setAge(in.readInt());
        in.readStringList(interests);
        setProfilePictureUri(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(getName());
        dest.writeInt(getFollowersNumber());
        dest.writeInt(getListsCreatedNumber());
        dest.writeInt(getListsFavouritedNumber());
        dest.writeString(getBio());
        dest.writeInt(getAge());
        dest.writeStringList(interests);
        dest.writeString(getProfilePictureUri());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
