package com.parse.starter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

/**
 * Created by Burak M on 1.5.2015.
 */
public class EventDisplay extends ActionBarActivity implements View.OnClickListener {
    private TextView tvOrganizer;
    private TextView tvOrganizerName;
    private TextView tvEventName;
    private TextView tvPerson;
    private TextView tvPersonValue;
    private TextView tvDate;
    private TextView tvDateValue;
    private TextView tvTime;
    private TextView tvTimeValue;
    private TextView tvQuota;
    private TextView tvQuotaValue;
    private TextView tvAddress;
    private TextView tvAddressValue;
    private TextView tvCity;
    private TextView tvCityValue;
    private TextView tvDescription;
    private TextView tvDescriptionValue;
    private TextView tvNumberOfSubscribers;
    private TextView tvNumberOfSubscribersValue;
    private CheckBox cbMeal;
    private CheckBox cbAlcohol;
    private ImageView ivOrganizerImage;
    private ImageView ivMap;
    private ImageView ivEventImage;
    private Button bBuyTicket;
    private Button bSubscribe;
    private Button bEdit;
    private Button bVideo;
    private Drawable organizerImage;
    private Drawable eventImage;
    private OtherEvent event;
    private int timeInCloud;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.event_display);
        initialise_variables();

        extras = getIntent().getExtras();
        String eventId = extras.getString( "eventId");
        String organizerId = extras.getString( "organizerId");

        Log.e( "category", extras.getString( "category"));

        if( extras.getString( "category").equals( "Cinema") )
        {
            event = new Cinema( eventId, this);
            tvPerson.setText("Director:");
            //if( (( Cinema)event).getDirectorName().length() <= 13)
                tvPersonValue.setText( (( Cinema)event).getDirectorName());
            //else
               // tvPersonValue.setText( (( Cinema)event).getDirectorName().substring( 0, 11) + "...");
        }
        else if( extras.getString( "category").equals( "Theatre"))
        {
            event = new Theatre( eventId, this);
            tvPerson.setText("Director:");
            //if( (( Theatre)event).getDirectorName().length() <= 13)
                tvPersonValue.setText( (( Theatre)event).getDirectorName());
            //else
                //tvPersonValue.setText( (( Theatre)event).getDirectorName().substring( 0, 11) + "...");
        }
        else if( extras.getString( "category").equals( "Seminar"))
        {
            event = new Seminar( eventId, this);
            tvPerson.setText( "Speaker:");
            //if( (( Seminar)event).getSpeaker().length() <= 16)
                tvPersonValue.setText( (( Seminar)event).getSpeaker());
            //else
                //tvPersonValue.setText( (( Seminar)event).getSpeaker().substring( 0, 14) + "...");
        }
        else if( extras.getString( "category").equals( "Concert"))
        {
            event = new Concert( eventId, this);
            tvPerson.setText("Artist:");
            //if( (( Concert)event).getArtist().length() <= 18)
                tvPersonValue.setText( (( Concert)event).getArtist());
            //else
                //tvPersonValue.setText( (( Concert)event).getArtist().substring( 0, 16) + "...");
        }
        else if( extras.getString( "category").equals( "Party"))
        {
            setContentView( R.layout.event_display_party);
            initialise_variables();
            cbMeal = (CheckBox) findViewById( R.id.cbMeal);
            cbAlcohol = (CheckBox) findViewById( R.id.cbAlcohol);
            event = new Party( eventId, this);
            tvPerson.setText( "DJ:");
            //if( (( Party)event).getDJ().length() <= 20)
                tvPersonValue.setText( (( Party)event).getDJ());
            //else
               // tvPersonValue.setText( (( Party)event).getDJ().substring( 0, 18) + "...");
            cbAlcohol.setChecked( (( Party)event).isAlcoholAllowed());
            cbMeal.setChecked( (( Party)event).hasMealService());
        }
        else    // other event category
        {
            setContentView( R.layout.event_display_others);
            initialise_variables();
            event = new OtherEvent( eventId, this);
        }

        if( organizerId.equals( ParseUser.getCurrentUser().getObjectId()))
            bEdit.setOnClickListener( this);
        else
            bEdit.setVisibility( View.GONE);

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

        tvDateValue.setText( event.getDate() % 100 + "/" + (event.getDate() % 10000 ) / 100  + "/"
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
        tvNumberOfSubscribersValue.setText( "" + event.getSubscribers().size());
        if( event.getQuota() != 0)
            tvQuotaValue.setText( "" + event.getQuota());
        else
            tvQuotaValue.setText( "no limit");

        if( event.getSubscribers().contains(ParseUser.getCurrentUser().getObjectId() ))
            bSubscribe.getBackground().setColorFilter( Color.parseColor("#121212"), PorterDuff.Mode.SRC);
        bBuyTicket.setOnClickListener(this);
        bVideo.setOnClickListener( this);
        bSubscribe.setOnClickListener( this);
        ivOrganizerImage.setOnClickListener( this);
        tvOrganizerName.setOnClickListener( this);
        ivMap.setOnClickListener( this);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("objectId", event.getParseID());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                ArrayList<String> subs = (ArrayList)parseObjects.get(0).getList("subscribers");
                if ( !subs.contains(ParseUser.getCurrentUser().getObjectId())) {
                    bSubscribe.getBackground().setColorFilter(Color.parseColor("#00E676"), PorterDuff.Mode.SRC);
                    bSubscribe.setText("Subscribe");
                }
                else {
                    bSubscribe.getBackground().setColorFilter(Color.parseColor("#878787"), PorterDuff.Mode.SRC);
                    bSubscribe.setText("UnSubscribe");
                }
            }
        });
        bBuyTicket.getBackground().setColorFilter( Color.parseColor("#00E676"), PorterDuff.Mode.SRC);

    }

    private void initialise_variables()
    {
        tvOrganizer = (TextView) findViewById( R.id.tvOrganizer);
        tvOrganizerName = (TextView) findViewById( R.id.tvOrganizerName);
        tvEventName = (TextView) findViewById( R.id.tvEventName);
        tvPerson = (TextView) findViewById( R.id.tvPerson);
        tvPersonValue = (TextView) findViewById( R.id.tvPersonValue);
        tvDate = (TextView) findViewById( R.id.tvDate);
        tvDateValue = (TextView) findViewById( R.id.tvDateValue);
        tvTime = (TextView) findViewById( R.id.tvTime);
        tvTimeValue = (TextView) findViewById( R.id.tvTimeValue);
        tvQuota = (TextView) findViewById( R.id.tvQuota);
        tvQuotaValue = (TextView) findViewById( R.id.tvQuotaValue);
        tvAddress = (TextView) findViewById( R.id.tvAddress);
        tvAddressValue = (TextView) findViewById( R.id.tvAddressValue);
        tvCity = (TextView) findViewById( R.id.tvCity);
        tvCityValue = (TextView) findViewById( R.id.tvCityValue);
        tvDescription = (TextView) findViewById( R.id.tvDescription);
        tvDescriptionValue = (TextView) findViewById( R.id.tvDescriptionValue);
        tvNumberOfSubscribers = (TextView) findViewById( R.id.tvNumberOfSubscribers);
        tvNumberOfSubscribersValue = (TextView) findViewById( R.id.tvNumberOfSubscribersValue);
        ivOrganizerImage = (ImageView) findViewById( R.id.ivOrganizerImage);
        ivMap = (ImageView) findViewById( R.id.ivMap);
        ivEventImage = (ImageView) findViewById( R.id.ivEventImage);
        bEdit = (Button) findViewById( R.id.bEdit);
        bVideo = (Button) findViewById( R.id.bVideo);
        bBuyTicket = (Button) findViewById( R.id.bBuyTicket);
        bSubscribe = (Button) findViewById( R.id.bSubscribe);
    }

        @Override
        public void onClick (View v)
        {
            if (v.getId() == bBuyTicket.getId())
            {
                if( Patterns.WEB_URL.matcher(event.getTicketLink()).matches()) {
                    Log.e("ticket: ", event.getTicketLink());
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getTicketLink()));
                        startActivity(browserIntent);
                    } catch ( ActivityNotFoundException e ){
                        Toast.makeText(this, "This link is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "The ticket link was not provided", Toast.LENGTH_LONG).show();
                }
            }
            else if (v.getId() == bVideo.getId())
            {
                Log.e("video: ", event.getVideoLink());
                if( Patterns.WEB_URL.matcher(event.getVideoLink()).matches()) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.getVideoLink()));
                        startActivity(browserIntent);
                    } catch( ActivityNotFoundException e){
                        Toast.makeText(this, "This link is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "The video link was not provided", Toast.LENGTH_LONG).show();
                }
            }
            else if (v.getId() == bSubscribe.getId())
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                query.whereEqualTo("objectId", event.getParseID());

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        ArrayList<String> subs = (ArrayList)parseObjects.get(0).getList("subscribers");
                        if ( !subs.contains(ParseUser.getCurrentUser().getObjectId())) {
                            subs.add(ParseUser.getCurrentUser().getObjectId());
                            parseObjects.get(0).saveInBackground();
                            tvNumberOfSubscribersValue.setText(subs.size() + "" );
                            bSubscribe.getBackground().setColorFilter(Color.parseColor("#878787"), PorterDuff.Mode.SRC);
                            bSubscribe.setText("UnSubscribe");
                            if( ParseUser.getCurrentUser().get("numSubscribe") != null ) {
                                ParseUser.getCurrentUser().increment("numSubscribe");
                                ParseUser.getCurrentUser().saveInBackground();
                            }
                            else{
                                ParseUser.getCurrentUser().put("numSubscribe", 0);
                                ParseUser.getCurrentUser().increment("numSubscribe");
                                ParseUser.getCurrentUser().saveInBackground();
                            }
                        }
                        else{
                            subs.remove(ParseUser.getCurrentUser().getObjectId());
                            parseObjects.get(0).saveInBackground();
                            tvNumberOfSubscribersValue.setText(subs.size() + "" );
                            bSubscribe.getBackground().setColorFilter(Color.parseColor("#00E676"), PorterDuff.Mode.SRC);
                            bSubscribe.setText("Subscribe");
                            if( ParseUser.getCurrentUser().get("numSubscribe") != null ) {
                                ParseUser.getCurrentUser().put("numSubscribe", ParseUser.getCurrentUser().getInt("numSubscribe") - 1);
                                ParseUser.getCurrentUser().saveInBackground();
                            }
                            else{
                                ParseUser.getCurrentUser().put("numSubscribe", 0);
                                ParseUser.getCurrentUser().put("numSubscribe", ParseUser.getCurrentUser().getInt("numSubscribe") - 1);
                                ParseUser.getCurrentUser().saveInBackground();
                            }
                        }
                    }

                });
            }
            else if (v.getId() == tvOrganizerName.getId() || v.getId() == ivOrganizerImage.getId())
            {
                Intent intent = new Intent(this, UserProfile.class);
                intent.putExtra("userId", event.getOrganizerID());
                startActivity(intent);
            }
            else if (v.getId() == ivMap.getId())
            {
                if( event.isMapClicked()) {
                    Intent map = new Intent(this, Maps.class);
                    map.putExtra("latitude", event.getLatitude());
                    map.putExtra("longitude", event.getLongitude());
                    map.putExtra("listener", false);
                    startActivity(map);
                }
                else
                    Toast.makeText( EventDisplay.this, "The precise location was not provided by the organizer.", Toast.LENGTH_LONG).show();
            }
            else if( v.getId() == bEdit.getId())
            {
                Intent edit = new Intent( this, EditEvent.class);
                edit.putExtra( "eventId", event.getParseID());
                edit.putExtra( "category", event.getCategory());
                if( !extras.getString( "category").equals( "Others"))
                    edit.putExtra( "tvPersonValue", tvPersonValue.getText().toString());
                startActivity( edit);
            }
        }

        @Override
        public boolean onCreateOptionsMenu (android.view.Menu menu)
        {
            // Inflate the menu items for use in the action bar
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_activity_actions, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item)
        {
            if (item.getItemId() == R.id.action_myprofile) {
                Intent userProfile = new Intent( this, UserProfile.class);
                userProfile.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
                startActivity( userProfile);
                return true;
            } else if (item.getItemId() == R.id.action_home) {
                Intent home = new Intent(this, Menu.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                finish();
                return true;
            } else if (item.getItemId() == R.id.action_settings) {
                Intent preferences = new Intent(this, Preferences.class);
                startActivity(preferences);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

}
