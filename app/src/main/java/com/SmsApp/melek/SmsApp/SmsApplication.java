package com.SmsApp.melek.SmsApp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class SmsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

       // Parse.initialize(this, "YEoJOFHlaTA7m5fKeIP4YqpOGQ0AD8NDSvqhnFi1", "XAzKjPrGp3B0zsifaMAP65WWqLNR3KJxD3Lzo0Sg");

        Parse.initialize(this, "EVl5JaKHPgADDSjRZDo4aQzN9NAxGr8eSWQ6U8v4", "vG9GhVqqcv52KWpXhlfk5CBkeLqj3MfmOgNP9EHo");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
