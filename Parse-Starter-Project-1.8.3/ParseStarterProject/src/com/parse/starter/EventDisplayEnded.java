package com.parse.starter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.*;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


/**
 * Created by Burak M on 3.5.2015.
 */
public class EventDisplayEnded extends ActionBarActivity implements View.OnClickListener
{
    private TextView tvOrganizer;
    private TextView tvOrganizerName;
    private TextView tvEventName;
    private TextView tvPerson;
    private TextView tvPersonValue;
    private TextView tvDate;
    private TextView tvDateValue;
    private TextView tvTime;
    private TextView tvTimeValue;
    private TextView tvAddress;
    private TextView tvAddressValue;
    private TextView tvCity;
    private TextView tvCityValue;
    private TextView tvRating;
    private TextView tvDiscription;
    private TextView tvNumberOfSubscribersValue;
    private TextView tvDescriptionValue;
    private Drawable organizerImage;
    private Drawable eventImage;
    private ImageView ivOrganizerImage;
    private ImageView ivMap;
    private ImageView ivEventImage;
    private Button bVideo;
    private Button bRate;
    private RatingBar ratingBar;
    private OtherEvent event;
    private int timeInCloud;
    private Bundle extras;
    //LayerDrawable stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.event_display_ended);
        initialise_variables();

        extras = getIntent().getExtras();
        String eventId = extras.getString( "eventId");

        Log.e( "category", extras.getString( "category"));

        if( extras.getString( "category").equals( "Cinema") )
        {
            tvPerson = (TextView) findViewById( R.id.tvPerson);
            tvPersonValue = (TextView) findViewById( R.id.tvPersonValue);
            event = new Cinema( eventId, this);
            tvPerson.setText("Director:");
            //if( (( Cinema)event).getDirectorName().length() <= 13)
                tvPersonValue.setText( (( Cinema)event).getDirectorName());
            //else
               // tvPersonValue.setText( (( Cinema)event).getDirectorName().substring( 0, 11) + "...");
        }
        else if( extras.getString( "category").equals( "Theatre"))
        {
            tvPerson = (TextView) findViewById( R.id.tvPerson);
            tvPersonValue = (TextView) findViewById( R.id.tvPersonValue);
            event = new Theatre( eventId, this);
            tvPerson.setText("Director:");
            //if( (( Theatre)event).getDirectorName().length() <= 13)
                tvPersonValue.setText( (( Theatre)event).getDirectorName());
            //else
                //tvPersonValue.setText( (( Theatre)event).getDirectorName().substring( 0, 11) + "...");
        }
        else if( extras.getString( "category").equals( "Seminar"))
        {
            tvPerson = (TextView) findViewById( R.id.tvPerson);
            tvPersonValue = (TextView) findViewById( R.id.tvPersonValue);
            event = new Seminar( eventId, this);
            tvPerson.setText( "Speaker:");
            //if( (( Seminar)event).getSpeaker().length() <= 16)
                tvPersonValue.setText( (( Seminar)event).getSpeaker());
            //else
                //tvPersonValue.setText( (( Seminar)event).getSpeaker().substring( 0, 14) + "...");
        }
        else if( extras.getString( "category").equals( "Concert"))
        {
            tvPerson = (TextView) findViewById( R.id.tvPerson);
            tvPersonValue = (TextView) findViewById( R.id.tvPersonValue);
            event = new Concert( eventId, this);
            tvPerson.setText("Artist:");
            //if( (( Concert)event).getArtist().length() <= 18)
                tvPersonValue.setText( (( Concert)event).getArtist());
            //else
                //tvPersonValue.setText( (( Concert)event).getArtist().substring( 0, 16) + "...");
        }
        else if( extras.getString( "category").equals( "Party"))
        {
            tvPerson = (TextView) findViewById( R.id.tvPerson);
            tvPersonValue = (TextView) findViewById( R.id.tvPersonValue);
            event = new Party( eventId, this);
            tvPerson.setText( "DJ:");
            //if( (( Party)event).getDJ().length() <= 20)
                tvPersonValue.setText( (( Party)event).getDJ());
            //else
               // tvPersonValue.setText( (( Party)event).getDJ().substring( 0, 18) + "...");
        }
        else    // other event category
        {
            setContentView( R.layout.event_display_ended_others);
            initialise_variables();
            event = new OtherEvent( eventId, this);
        }

        ratingBar.setClickable(true);
        ratingBar.setMax(5);
        ratingBar.setStepSize(Float.parseFloat(0.5 + ""));
        ratingBar.setOnClickListener(this);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("Hey: ", "you");
                    Intent intent = new Intent(EventDisplayEnded.this, CommentList.class);
                    intent.putExtra("key", "eventID");
                    intent.putExtra("value", EventDisplayEnded.this.event.getParseID());
                    startActivity(intent);
                }
                return true;
            }
        });
        ratingBar.setRating(Float.parseFloat( event.getRating()+""));
        Log.e("rating", event.getRating() + "");

        if( event.getOrganizerName().length() <= 16)
            tvOrganizerName.setText( event.getOrganizerName());
        else
            tvOrganizerName.setText( event.getOrganizerName().substring( 0, 14) + "...");

        organizerImage = event.getOrganizerImage();
        ivOrganizerImage.setImageDrawable( organizerImage);

        //if( event.getName().length() <= 11)
            tvEventName.setText( event.getName());
       // else
            //tvEventName.setText( event.getName().substring( 0, 9) + "...");
        eventImage = event.getEventImage();
        ivEventImage.setImageDrawable( eventImage);

        tvDateValue.setText(  event.getDate() % 100 + "/" + (event.getDate() % 10000 ) / 100  + "/"
                    + event.getDate() / 10000);
        timeInCloud = event.getTime();
        if( timeInCloud / 100 <= 9)
        {
            if( timeInCloud % 100 <= 9)
                tvTimeValue.setText("0" + timeInCloud / 100  + ":0" + timeInCloud % 100);
            else
                tvTimeValue.setText("0" + timeInCloud / 100  + ":" + timeInCloud % 100);
        }
        else
        {
            if( timeInCloud % 100 <= 9)
                tvTimeValue.setText("" + timeInCloud / 100  + ":0" + timeInCloud % 100);
            else
                tvTimeValue.setText("" + timeInCloud / 100  + ":" + timeInCloud % 100);
        }
        tvAddressValue.setText( event.getAddress());
        tvCityValue.setText( event.getCity());
        tvDescriptionValue.setText( event.getDescription());
        bVideo.setOnClickListener( this);
        bRate.setOnClickListener( this);
        ivOrganizerImage.setOnClickListener( this);
        tvOrganizerName.setOnClickListener(this);
        ivMap.setOnClickListener(this);

    }

    private void initialise_variables()
    {
        tvOrganizer = (TextView) findViewById( R.id.tvOrganizer);
        tvOrganizerName = (TextView) findViewById( R.id.tvOrganizerName);
        tvEventName = (TextView) findViewById( R.id.tvEventName);
        tvDate = (TextView) findViewById( R.id.tvDate);
        tvDateValue = (TextView) findViewById( R.id.tvDateValue);
        tvTime = (TextView) findViewById( R.id.tvTime);
        tvTimeValue = (TextView) findViewById( R.id.tvTimeValue);
        tvAddress = (TextView) findViewById( R.id.tvAddress);
        tvAddressValue = (TextView) findViewById( R.id.tvAddressValue);
        tvCity = (TextView) findViewById( R.id.tvCity);
        tvCityValue = (TextView) findViewById( R.id.tvCityValue);
        tvRating = (TextView) findViewById( R.id.tvRating);
        tvDiscription = (TextView) findViewById( R.id.tvDescription);
        tvDescriptionValue = (TextView) findViewById( R.id.tvDescriptionValue);
        ivOrganizerImage = (ImageView) findViewById( R.id.ivOrganizerImage);
        ivMap = (ImageView) findViewById( R.id.ivMap);
        ivEventImage = (ImageView) findViewById( R.id.ivEventImage);
        bVideo = (Button) findViewById( R.id.bVideo);
        bRate = (Button) findViewById( R.id.bRate);
        ratingBar = (RatingBar) findViewById( R.id.ratingBar);
        //stars = (LayerDrawable) ratingBar.getProgressDrawable();
    }

    @Override
    public void onClick(View v)
    {
        if( v.getId() == bVideo.getId())
        {
            if( Patterns.WEB_URL.matcher( event.getVideoLink()).matches()) {
                Log.e("EventDisplayEnded: ", event.getVideoLink());
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getVideoLink()));
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e ){
                    Toast.makeText(this, "This link is not available!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "The Video link was not provided", Toast.LENGTH_LONG);
            }
        }
        else if( v.getId() == bRate.getId())
        {
            Intent addComment = new Intent( this, AddComment.class);
            addComment.putExtra( "eventId", event.getParseID());
            addComment.putExtra( "category", event.getCategory());
            addComment.putExtra( "eventKey", event.getKey());
            startActivity( addComment);
        }
        else if( v.getId() == tvOrganizerName.getId() || v.getId() == ivOrganizerImage.getId())
        {
            Intent intent = new Intent( this, UserProfile.class);
            intent.putExtra("userId", event.getOrganizerID());
            startActivity(intent);
        }
        else if( v.getId() == ivMap.getId())
        {
            if( event.isMapClicked()) {
                Intent map = new Intent(this, Maps.class);
                map.putExtra("latitude", event.getLatitude());
                map.putExtra("longitude", event.getLongitude());
                startActivity(map);
            }
            else
                Toast.makeText( EventDisplayEnded.this, "The precise location was not provided by the organizer.", Toast.LENGTH_LONG).show();
        }
        else if( v == ivOrganizerImage){
            Intent intent = new Intent( this, UserProfile.class);
            intent.putExtra("userId", event.getOrganizerID());
            startActivity(intent);
        }
    }
/*
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
        if( rating - 1f < 0.01)
        {
            stars.getDrawable(1).setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
        }
        else if( rating - 2.5f < 0.01)
        {
            stars.getDrawable(1).setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
        }
        else if( rating - 3.5f < 0.01)
        {
            stars.getDrawable(1).setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
        }
        else if( rating - 4f < 0.01)
        {
            stars.getDrawable(1).setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
        }
        else if( rating - 5f < 0.01)
        {
            stars.getDrawable(1).setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu( Menu menu)
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
            Intent home = new Intent( this, com.parse.starter.Menu.class);
            home.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity( home);
            finish();
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
