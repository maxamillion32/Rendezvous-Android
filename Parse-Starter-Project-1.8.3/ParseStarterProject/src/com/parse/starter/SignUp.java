package com.parse.starter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import bolts.Task;

/**
 * Created by Burak M on 16.3.2015.
 */
public class SignUp extends Activity implements View.OnClickListener
{
    private ImageView logo;
    private TextView  appName;
    private EditText  userName;
    private EditText  eMail;
    private EditText  password;
    private EditText  rePassword;
    private Button    createAccount;

    private String    strUserName;
    private String    strEmail;
    private String    strPassword;
    private String    strRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.signup);
        initialise_variables();

        createAccount.setOnClickListener( this);
    }

    private void initialise_variables()
    {
        logo = (ImageView) findViewById( R.id.iwLogo);
        appName = (TextView) findViewById( R.id.tvSignupRendezvous);
        userName = (EditText) findViewById( R.id.etSignName);
        eMail = (EditText) findViewById( R.id.etSignEmail);
        password = (EditText) findViewById( R.id.etSignPassword);
        rePassword = (EditText) findViewById( R.id.etSignRe_password);
        createAccount = (Button) findViewById( R.id.bCreateAccount);
    }

    @Override
    public void onClick(View v)
    {
        if( v.getId() == createAccount.getId())
        {
            strUserName = userName.getText().toString();
            strEmail = eMail.getText().toString();
            strPassword = password.getText().toString();
            strRePassword = rePassword.getText().toString();

            if( strUserName.length() == 0)
                showToast( "User name is required! Please enter a user name.", Toast.LENGTH_SHORT);
            else if( strEmail.length() == 0)
                showToast( "Email is required! Please enter an email address.", Toast.LENGTH_SHORT);
            else if( strPassword.length() == 0)
                showToast( "Password cannot be empty!", Toast.LENGTH_SHORT);
            else if( !strPassword.equals( strRePassword))
                showToast("Passwords are not same.", Toast.LENGTH_SHORT);
            else
            {
                ParseUser user = new ParseUser();
                user.setUsername( strUserName);
                user.setEmail(strEmail);
                user.setPassword(strPassword);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)  //signed up
                        {
                            SignUp.this.finish();
                            showToast( "Your account has been created successfully. Please go your inbox and " +
                                    "verify your email address.", Toast.LENGTH_LONG);
                        }
                        else
                            showToast( e.toString().substring( 26) , Toast.LENGTH_LONG);
                    }

                });
            }
        }

    }

    private void showToast( CharSequence message, int duration)
    {
        Toast.makeText( this, message, duration).show();
    }
}
