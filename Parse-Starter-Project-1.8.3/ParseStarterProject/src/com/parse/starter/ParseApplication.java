package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "nIRB4PVe5bFaVrrMJtkSQdyAGHPLwCftRn5gjsRY", "yd2jbLP50yDv5m1jQ9NGGI4Ds8AF4DH2055C6hNy");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
