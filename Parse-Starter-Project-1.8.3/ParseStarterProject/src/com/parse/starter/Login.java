package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import bolts.Task;

/**
 * Created by Burak M on 15.3.2015.
 */
public class Login extends Activity implements View.OnClickListener
{
    private ImageView logo;
    private TextView  appName;
    private EditText  userName;
    private EditText  password;
    private TextView  forgotPass;
    private CheckBox  rememberMe;
    private Button    login;
    private Button    signup;


    private String strUserName;
    private String strPassword;

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.login);
        initialise_variables();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        /*SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( getBaseContext());
        if( preferences.getBoolean( "pref_rememberMe", false))  // if rememberMe was selected*/

        boolean fromSplash = getIntent().getBooleanExtra( "connection", true);
        if( !fromSplash)
            Toast.makeText(this, "Check your Internet connection" ,Toast.LENGTH_LONG).show();

        forgotPass.setOnClickListener(this);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    private void initialise_variables()
    {
        logo = (ImageView) findViewById( R.id.iwLogo);
        appName = (TextView) findViewById( R.id.tvLoginRendezvous);
        forgotPass = (TextView) findViewById( R.id.tvForgotPassword);
        rememberMe = (CheckBox) findViewById( R.id.cbRememberMe);
        userName = (EditText) findViewById( R.id.etUserName);
        password = (EditText) findViewById( R.id.etPassword);
        login = (Button) findViewById( R.id.bLogin);
        signup = (Button) findViewById( R.id.bSignup);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == login.getId())
        {
            strUserName = userName.getText().toString();
            strPassword = password.getText().toString();

            if( !isNetworkAvailable())  // if network connections is not available
                Toast.makeText(this, "Check your Internet connection" ,Toast.LENGTH_LONG).show();
            else
            {
                ParseUser.logInInBackground( strUserName, strPassword, new LogInCallback() {

                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null)  // logged in
                        {
                            if (!(boolean) (parseUser.get( "emailVerified"))) // if email is not verified
                                showToast( "Please verify your email address first.", Toast.LENGTH_LONG);
                            else    // verified user
                            {
                                Intent menu = new Intent(Login.this, Menu.class);
                                /*menu.putExtra( "username", strUserName);
                                menu.putExtra( "password", strPassword);*/
                                if( rememberMe.isChecked())
                                {
                                    getSharedPreferences( "REMEMBER_ME", MODE_PRIVATE).edit()
                                        .putBoolean( "remember", true)
                                        .commit();
                                    getSharedPreferences("ACCOUNT_CREDENTIALS", MODE_PRIVATE).edit()
                                        .putString("username", strUserName)
                                        .putString("password", strPassword)
                                        .commit();
                                }
                                else
                                    getSharedPreferences( "REMEMBER_ME", MODE_PRIVATE).edit()
                                        .putBoolean( "remember", false)
                                        .commit();
                                Login.this.finish();
                                startActivity( menu);
                            }
                        } else    // something went wrong
                            showToast( e.toString().substring(26), Toast.LENGTH_LONG);
                    }
                });
            }
        }
        else if (v.getId() == signup.getId())
        {
            if( !isNetworkAvailable())  // if network connections is not available
                Toast.makeText(this, "Check your Internet connection" ,Toast.LENGTH_LONG).show();
            else
            {
                Intent signUp = new Intent("android.intent.action.SIGNUP");
                startActivity(signUp);
            }
        }
        else if (v.getId() == forgotPass.getId())
        {
             if( !isNetworkAvailable())  // if network connections is not available
                Toast.makeText(this, "Check your Internet connection" ,Toast.LENGTH_LONG).show();
            else
             {
                 if( userName.getText().toString().equals( ""))
                     Toast.makeText( this, "Enter your username to reset your password", Toast.LENGTH_SHORT).show();
                 else
                 {
                     Intent resetPassword = new Intent( this, ResetPassword.class);
                     resetPassword.putExtra( "username", userName.getText().toString());
                     startActivity(resetPassword);
                 }
             }
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
