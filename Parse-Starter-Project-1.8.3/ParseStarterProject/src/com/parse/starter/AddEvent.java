package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;


public class AddEvent extends ActionBarActivity implements View.OnClickListener {
    Button cinema, concert, theatre, seminar, party, other;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_add_event);
        cinema = (Button) findViewById(R.id.cinema);
        cinema.setOnClickListener(this);
        concert = (Button) findViewById(R.id.concert);
        concert.setOnClickListener(this);
        theatre = (Button) findViewById(R.id.theatre);
        theatre.setOnClickListener(this);
        seminar = (Button) findViewById(R.id.seminar);
        seminar.setOnClickListener(this);
        party = (Button) findViewById(R.id.party);
        party.setOnClickListener(this);
        other = (Button) findViewById(R.id.other);
        other.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == cinema) {
            Intent cinema = new Intent(AddEvent.this, AddCinema.class);
            AddEvent.this.startActivity(cinema);
        }
        if (v == concert) {
            Intent concert = new Intent(AddEvent.this, AddConcert.class);
            AddEvent.this.startActivity(concert);
        }
        if (v == theatre) {
            Intent theatre = new Intent(AddEvent.this, AddTheatre.class);
            AddEvent.this.startActivity(theatre);
        }
        if (v == seminar) {
            Intent seminar = new Intent(AddEvent.this, AddSeminar.class);
            AddEvent.this.startActivity(seminar);
        }
        if (v == party) {
            Intent conference = new Intent(AddEvent.this, AddParty.class);
            AddEvent.this.startActivity(conference);
        }
        if (v == other) {
            Intent other = new Intent(AddEvent.this, AddOther.class);
            AddEvent.this.startActivity(other);
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
            Intent intent = new Intent( this, Menu.class );
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