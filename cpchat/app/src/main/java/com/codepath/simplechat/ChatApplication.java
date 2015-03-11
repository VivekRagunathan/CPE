package com.codepath.simplechat;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by abgandhi on 3/4/15.
 */
public class ChatApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "28U7tLYLUDQ876FIbLbh5bNUfD5iYnTWYkOEEwra";
    public static final String YOUR_CLIENT_KEY = "LLw2rx6hFDgj4PriZw3OFcwPU7zGqPZbPbsBOaH6";
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}