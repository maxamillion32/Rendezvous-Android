package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Burak M on 1.5.2015.
 */
public class Loading extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.loading);

        Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Loading.this.finish();
                    }
                }
            };
        timer.start();
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
            Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
            /*Intent addEvent = new Intent( this, AddEvent.class);
            startActivity( addEvent);
            return true;*/
        }
        else if( item.getItemId() == R.id.action_home)
        {
            Intent menu = new Intent( this, Menu.class);
            startActivity( menu);
            return true;
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
