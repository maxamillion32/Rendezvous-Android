package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by Burak M on 2.5.2015.
 */
public class ResetPassword extends Activity
{
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent fromLogin = getIntent();
        Bundle username = fromLogin.getExtras();

        if( username != null)   // if forgot password was clicked
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder( ResetPassword.this);
            builder1.setMessage( "Are you sure to reset your password?");
            builder1.setCancelable( true);
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder( ResetPassword.this);
                    final EditText input = new EditText( ResetPassword.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    builder2.setView( input);
                    builder2.setMessage( "Enter your email address");
                    builder2.setCancelable(true);
                    builder2.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if( input.getText().toString().isEmpty())
                                Toast.makeText( ResetPassword.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                            else
                            {
                                email = input.getText().toString();
                                ParseUser user = new ParseUser();
                                ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                                    public void done(com.parse.ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(ResetPassword.this, "An email was sent to your email address to reset your password.", Toast.LENGTH_SHORT).show();
                                            getSharedPreferences("REMEMBER_ME", MODE_PRIVATE).edit().putBoolean("remember", false).apply();
                                        } else {
                                            Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                ResetPassword.this.finish();
                            }
                        }
                    });
                    builder2.setNegativeButton( "Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            ResetPassword.this.finish();
                        }
                    });
                    AlertDialog alert2 = builder2.create();
                    alert2.show();
                }
            });
            builder1.setNegativeButton( "No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    ResetPassword.this.finish();
                }
            });

            AlertDialog alert = builder1.create();
            alert.show();
        }
        else    // if password change request was made
        {
            email = ParseUser.getCurrentUser().getEmail();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ResetPassword.this);
            builder1.setMessage("Are you sure to reset your password?");
            builder1.setCancelable(true);
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Toast.makeText(ResetPassword.this, "An email was sent to your email address to reset your password.", Toast.LENGTH_SHORT).show();
                                getSharedPreferences("REMEMBER_ME", MODE_PRIVATE).edit().putBoolean("remember", false).apply();
                            } else {
                                Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    ResetPassword.this.finish();
                }
            });
            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    ResetPassword.this.finish();
                }
            });

            AlertDialog alert = builder1.create();
            alert.show();
        }

    }
}
