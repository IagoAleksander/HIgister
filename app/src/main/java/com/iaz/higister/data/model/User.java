package com.iaz.higister.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alksander on 17/03/2018.
 */

public class User {

    public String name;
    public int followersNumber;
    public ArrayList<User> followers;
    public int listsCreatedNumber;
    public ArrayList<UserList> listsCreated;
    public int listsFavouritedNumber;
//    public ArrayList<UserList> listsFavorited;
    public String description;
    public int age;
    public ArrayList<String> interests = new ArrayList<>();



}
