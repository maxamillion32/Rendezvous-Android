package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.parse.ParseUser;

/**
 * Created by Burak M on 20.3.2015.
 */
public class Logout extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ParseUser.logOut();

        // delete all preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();
        getSharedPreferences( "REMEMBER_ME", MODE_PRIVATE).edit().putBoolean( "remember", false).apply();

        Intent intent = new Intent( this, Menu.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities

        startActivity(intent);
        finish();
    }
}
