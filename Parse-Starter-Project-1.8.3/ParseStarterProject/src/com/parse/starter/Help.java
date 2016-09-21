package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class Help extends ActionBarActivity implements View.OnClickListener
{
    TextView account,profile,add,search,settings,verification,ratings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        account = (TextView) findViewById(R.id.account);
        account.setOnClickListener(this);
        profile = (TextView) findViewById(R.id.profile);
        profile.setOnClickListener(this);
        add = (TextView) findViewById(R.id.add_event);
        add.setOnClickListener(this);
        search = (TextView) findViewById(R.id.search_event);
        search.setOnClickListener(this);
        settings = (TextView) findViewById(R.id.settings);
        settings.setOnClickListener(this);
        verification = (TextView) findViewById(R.id.verification);
        verification.setOnClickListener(this);
        ratings = (TextView) findViewById(R.id.ratings);
        ratings.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        if(v == account)
        {
            Intent account = new Intent(Help.this, Account.class);
            startActivity(account);
        }

        if(v == profile)
        {
            Intent profile = new Intent(Help.this,Profile.class);
            startActivity(profile);
        }

        if(v == add)
        {
            Intent add = new Intent(Help.this,Adding_Event.class);
            startActivity(add);
        }

        if(v == search)
        {
            Intent search = new Intent(Help.this,Searching_Event.class);
            startActivity(search);
        }

        if(v == settings)
        {
            Intent settings = new Intent(Help.this,Settings.class);
            startActivity(settings);
        }

        if(v == verification)
        {
            Intent verification = new Intent(Help.this,Verification.class);
            startActivity(verification);
        }

        if(v == ratings)
        {
            Intent ratings = new Intent(Help.this, Ratings.class);
            startActivity(ratings);
        }
    }
    @Override
    public boolean onCreateOptionsMenu( android.view.Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item)
    {
        if( item.getItemId() == R.id.action_myprofile)
        {
            Intent userProfile = new Intent( this, UserProfile.class);
            userProfile.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
            startActivity( userProfile);
            return true;
        }
        else if( item.getItemId() == R.id.action_home)
        {
            Intent intent = new Intent( this, com.parse.starter.Menu.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity( intent );
            finish();
        }
        else if( item.getItemId() == R.id.action_settings)
        {
            Intent preferences = new Intent( this, Preferences.class);
            startActivity( preferences);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
