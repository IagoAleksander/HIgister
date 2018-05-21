package com.iaz.HIgister.data.model.MyAnimeList;

import java.util.ArrayList;

/**
 * Created by alksander on 02/03/2018.
 */

public class MyAnimeListResponse {

    private ArrayList<Result> result;

//    private String result_last_page;

    public ArrayList<Result> getResult ()
    {
        return result;
    }

    public void setResult (ArrayList<Result> result)
    {
        this.result = result;
    }

//    public String getResult_last_page ()
//    {
//        return result_last_page;
//    }
//
//    public void setResult_last_page (String result_last_page)
//    {
//        this.result_last_page = result_last_page;
//    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [result = "+result+", result_last_page = "+result_last_page+"]";
//    }
}
