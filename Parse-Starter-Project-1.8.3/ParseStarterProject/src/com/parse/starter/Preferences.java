package com.parse.starter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
/**
 * Created by Burak M on 18.3.2015.
 */
public class Preferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    //private String email;

    // can be extended PreferenceFragment instead, but it requires API 11 or more.
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        if( ParseUser.getCurrentUser().getEmail() != null)
            findPreference( "pref_editAccount").setSummary( ParseUser.getCurrentUser().getEmail());
        if( ParseUser.getCurrentUser().getString("name") != null)
            findPreference( "change_name").setSummary( ParseUser.getCurrentUser().getString( "name"));
        if( ParseUser.getCurrentUser().getUsername() != null)
            findPreference( "change_username").setSummary( ParseUser.getCurrentUser().getUsername());
        if( ParseUser.getCurrentUser().getEmail() != null)
            findPreference( "change_email").setSummary( ParseUser.getCurrentUser().getEmail());
        if( ParseUser.getCurrentUser().getString( "phone") != null)
            findPreference( "change_phone").setSummary( ParseUser.getCurrentUser().getString( "phone"));
        if( ParseUser.getCurrentUser().getString("address") != null)
            findPreference( "change_address").setSummary(ParseUser.getCurrentUser().getString( "address"));
        if( ParseUser.getCurrentUser().getString("city") != null)
            findPreference( "pref_city").setSummary( ParseUser.getCurrentUser().getString( "city"));
        if( ParseUser.getCurrentUser().getString("city") != null)
            ((ListPreference) findPreference( "pref_city")).setValueIndex( findIndexOfCity( ParseUser.getCurrentUser().getString( "city")));
        if( ParseUser.getCurrentUser().getString("notifyme") != null)
        {
            findPreference("pref_notifyme").setSummary(changeSummaryForNotifyMe());
            ((ListPreference) findPreference("pref_notifyme")).setValueIndex(findIndexOfNotifyme(changeSummaryForNotifyMe()));
        }
        ((SwitchPreference)findPreference( "pref_email_notification")).setChecked( ParseUser.getCurrentUser().getBoolean( "hasEmail"));
        ((SwitchPreference)findPreference( "pref_push_notification")).setChecked(ParseUser.getCurrentUser().getBoolean( "hasPush"));


        /*Intent intentFromMenu = getIntent();
        email = intentFromMenu.getStringExtra( "email");*/

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener( this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if( key.equals( "change_name"))
        {
            ParseUser.getCurrentUser().put( "name", sharedPreferences.getString( key, (String) ParseUser.getCurrentUser().get( "name")));
            ParseUser.getCurrentUser().saveInBackground();
            findPreference( key).setSummary( ParseUser.getCurrentUser().getString( "name"));
            Toast.makeText( this, "Name is changed to "
                    + sharedPreferences.getString( key, ""), Toast.LENGTH_SHORT).show();
        }
        /*else if( key.equals( "change_username"))
        {
            Toast.makeText( this, "Username cannot be changed", Toast.LENGTH_SHORT).show();
            findPreference( key).setSummary( sharedPreferences.getString( key, ""));
            ParseUser.getCurrentUser().setUsername( sharedPreferences.getString( key, ParseUser.getCurrentUser().getUsername()));
            ParseUser.getCurrentUser().saveInBackground();
            Toast.makeText( this, "Username is changed to " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
            getSharedPreferences("ACCOUNT_CREDENTIALS", MODE_PRIVATE).edit()
                                        .putString("username", ParseUser.getCurrentUser().getUsername())
                                        .commit();
        }*/
        else if( key.equals( "change_email"))
        {
            ParseUser.getCurrentUser().setEmail( sharedPreferences.getString( key, ParseUser.getCurrentUser().getEmail()));
            ParseUser.getCurrentUser().saveInBackground();
            findPreference( key).setSummary( ParseUser.getCurrentUser().getEmail());
            Toast.makeText( this, "Email is changed to " + ParseUser.getCurrentUser().getEmail()
                    + ". Please verify your new email address now.", Toast.LENGTH_SHORT).show();
            getSharedPreferences( "REMEMBER_ME", MODE_PRIVATE).edit().putBoolean( "remember", false).apply();
        }
        else if( key.equals( "change_phone"))
        {
            ParseUser.getCurrentUser().put( "phone", sharedPreferences.getString( key, ParseUser.getCurrentUser().getString( "phone")));
            ParseUser.getCurrentUser().saveInBackground();
            findPreference( key).setSummary( ParseUser.getCurrentUser().getString( "phone"));
            Toast.makeText( this, "Phone number is changed to "
                    + sharedPreferences.getString( key, ""), Toast.LENGTH_SHORT).show();
        }
        else if( key.equals( "change_address"))
        {
            ParseUser.getCurrentUser().put( "address", sharedPreferences.getString( key, ParseUser.getCurrentUser().getString( "address")));
            ParseUser.getCurrentUser().saveInBackground();
            findPreference( key).setSummary( ParseUser.getCurrentUser().getString( "address"));
            Toast.makeText( this, "Address is changed to "
                    + sharedPreferences.getString( key, ""), Toast.LENGTH_SHORT).show();
        }
        else if( key.equals( "pref_language"))
        {
            Toast.makeText( Preferences.this, "Will be implemented soon...", Toast.LENGTH_SHORT).show();
        }
        else if( key.equals( "pref_city"))
        {
            ParseUser.getCurrentUser().put( "city", sharedPreferences.getString( key, ParseUser.getCurrentUser().getString( "city")));
            ParseUser.getCurrentUser().saveInBackground();
            Toast.makeText( this, "City is changed to "
                    + sharedPreferences.getString( key, ParseUser.getCurrentUser().getString( "city")), Toast.LENGTH_SHORT).show();
            findPreference( key).setSummary( sharedPreferences.getString( key, ParseUser.getCurrentUser().getString( "city")));
        }
        else if( key.equals( "pref_notifyme"))
        {
            ParseUser.getCurrentUser().put( "notifyme", sharedPreferences.getString( key, ParseUser.getCurrentUser().getString( "notifyme")));
            ParseUser.getCurrentUser().saveInBackground();
            Toast.makeText( this, "You will be notified before " + changeSummaryForNotifyMe(), Toast.LENGTH_SHORT).show();
            findPreference( key).setSummary( changeSummaryForNotifyMe());
        }
        else if( key.equals( "pref_email_notification"))
        {
            ParseUser.getCurrentUser().put( "hasEmail", sharedPreferences.getBoolean( key, ParseUser.getCurrentUser().getBoolean( "hasEmail")));
            ParseUser.getCurrentUser().saveInBackground();
        }
        else if ( key.equals( "pref_push_notification"))
        {
            ParseUser.getCurrentUser().put( "hasPush", sharedPreferences.getBoolean( key, ParseUser.getCurrentUser().getBoolean( "hasPush")));
            ParseUser.getCurrentUser().saveInBackground();
        }
    }

    public String changeSummaryForNotifyMe()
    {
        Integer notifyTime = Integer.parseInt( ParseUser.getCurrentUser().getString( "notifyme"));
        if( notifyTime == 30)
        {
            return "30 Minutes";
        }
        else if( notifyTime == 60)
        {
            return "1 Hour";
        }
        else if( notifyTime == 180)
        {
            return "3 Hours";
        }
        else if( notifyTime == 300)
        {
            return "5 Hours";
        }
        else if( notifyTime == 1440)
        {
            return "1 Day";
        }
        else if( notifyTime == 4320)
        {
            return "3 Days";
        }
        else if( notifyTime == 10080)
        {
            return "1 Week";
        }
        else if( notifyTime == 20160)
        {
            return "2 Weeks";
        }
        return "";
    }

    public int findIndexOfCity( String city)
    {
        if( city.equals( "Ankara"))
            return 0;
        else if( city.equals( "İstanbul"))
            return 1;
        else if( city.equals( "İzmir"))
            return 2;
        else if( city.equals( "Bursa"))
            return 3;
        else if( city.equals( "Kütahya"))
            return 4;
        return -1;
    }

    public int findIndexOfNotifyme( String value)
    {
        if( value.equals( "30 Minutes"))
        {
            return 0;
        }
        else if( value.equals( "1 Hour"))
        {
            return 1;
        }
        else if( value.equals( "3 Hours"))
        {
            return 2;
        }
        else if( value.equals( "5 Hours"))
        {
            return 3;
        }
        else if( value.equals( "1 Day"))
        {
            return 4;
        }
        else if( value.equals( "3 Days"))
        {
            return 5;
        }
        else if( value.equals( "1 Week"))
        {
            return 6;
        }
        else if( value.equals( "2 Weeks"))
        {
            return 7;
        }
        return -1;
    }
}
