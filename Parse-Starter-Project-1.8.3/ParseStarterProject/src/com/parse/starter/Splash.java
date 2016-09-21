package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Burak M on 28.4.2015.
 */
public class Splash extends Activity
{
    private String strUserName;
    private String strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.splash);

        if (!isNetworkAvailable())  // if there is no Internet connection
        {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent login = new Intent(Splash.this, Login.class);
                        login.putExtra( "connection", false);
                        Splash.this.finish();
                        startActivity( login);
                    }
                }
            };
            timer.start();
        }
        else {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (getSharedPreferences("REMEMBER_ME", MODE_PRIVATE).getBoolean("remember", false))  // if rememberMe was selected
                        {
                            SharedPreferences accountPreferences = getSharedPreferences("ACCOUNT_CREDENTIALS", MODE_PRIVATE);
                            strUserName = accountPreferences.getString("username", "");
                            strPassword = accountPreferences.getString("password", "");

                            ParseUser.logInInBackground(strUserName, strPassword, new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (parseUser != null)  // logged in
                                    {
                                        Intent menu = new Intent(Splash.this, Menu.class);
                                        showToast("Logged in as " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG);
                                        Splash.this.finish();
                                        startActivity(menu);

                                    } else    // something went wrong
                                        showToast(e.toString().substring(26), Toast.LENGTH_LONG);
                                }
                            });
                        } else {
                            Intent login = new Intent(Splash.this, Login.class);
                            Splash.this.finish();
                            startActivity(login);
                        }
                    }
                }
            };
            timer.start();
        }
    }
    private void showToast( CharSequence message, int duration)
    {
        Toast.makeText( this, message, duration).show();
    }
        protected boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if ( networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }
}
