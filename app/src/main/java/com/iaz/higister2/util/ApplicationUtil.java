package com.iaz.higister2.util;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Adenilson on 4/24/2017.
 */

public class ApplicationUtil {
    private static Context context;
    private static CompositeDisposable taskContainer;

    private ApplicationUtil(){
        super();
    }

    public static void setContext(Context context){

        ApplicationUtil.context = context;
    }

    public static Context getContext(){

        return ApplicationUtil.context;
    }

    public static void setTaskContainer(CompositeDisposable compositeDisposable){
        taskContainer = compositeDisposable;
    }

    public static CompositeDisposable getTaskContainer() {
        return taskContainer;
    }
}
