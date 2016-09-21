package com.parse.starter;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import bolts.Task;


public class AddComment extends ActionBarActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private EditText etComment,key;
    private TextView tvRatingBar;
    private RatingBar ratingBar;
    private String comment_text;
    private int eventKey, key_text;
    private float rate_value;
    private Button add_comment;
    private String eventID, category;
   // private LayerDrawable stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        etComment= (EditText)findViewById(R.id.comment);
        key = (EditText) findViewById(R.id.key);

        add_comment = (Button) findViewById(R.id.add_comment);
        add_comment.setOnClickListener(this);


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        stars = (LayerDrawable) ratingBar.getProgressDrawable();

        ratingBar.setStepSize( 0.5f );
        ratingBar.setNumStars(5);
        ratingBar.setRating( 1.0f );
        ratingBar.setOnRatingBarChangeListener(this);

        tvRatingBar = (TextView) findViewById(R.id.tvRatingBar);

        eventID = this.getIntent().getStringExtra("eventId");
        category = this.getIntent().getStringExtra("category");
        eventKey = this.getIntent().getIntExtra("eventKey", 0);
        Log.e("Key: ", eventKey + "");
    }

    @Override
    public void onClick(View v)
    {

        if(v == add_comment)
        {
            if( key.getText() == null || key.getText().toString().equals("") )
                Toast.makeText(this, "Please enter a key", Toast.LENGTH_SHORT);
            else
                key_text = Integer.parseInt( key.getText().toString() );

            if( key_text != eventKey)
                Toast.makeText(this, "Invalid Key!", Toast.LENGTH_LONG).show();
            else {
                ParseObject comment = new ParseObject("Comment");
                comment.put("commentatorName", ParseUser.getCurrentUser().getString("username"));
                comment.put("rating", ratingBar.getRating());
                comment.put("commentText", etComment.getText().toString());
                comment.put("commentatorPhoto", ParseUser.getCurrentUser().getParseFile("profilePhoto"));
                comment.put("commentatorName", ParseUser.getCurrentUser().getString("username"));
                comment.put("commentatorID", ParseUser.getCurrentUser().getObjectId() );
                Log.e("eventID:", eventID);
                comment.put("eventID", eventID);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                Task<ParseObject> b = query.getInBackground( eventID );
                while( !b.isCompleted());
                double rating;
                int numComment = b.getResult().getInt("numComment");
                rating = b.getResult().getDouble("rating") * numComment + ratingBar.getRating();
                rating = rating / ( numComment + 1 ) ;
                b.getResult().put("rating" ,rating);
                b.getResult().increment("numComment");
                b.getResult().saveInBackground();

                comment.saveInBackground();

                Toast.makeText(this, "Your comment has been added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
        if (rating - 1f < 0.01) {

           // stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
            tvRatingBar.setText("Your Rating: " + ratingBar.getRating());

        } else if (rating - 2.5f < 0.01) {
           // stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
            tvRatingBar.setText("Your Rating: " + ratingBar.getRating());

        } else if (rating - 3.5f < 0.01) {
          //  stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
            tvRatingBar.setText("Your Rating: " + ratingBar.getRating());

        } else if (rating - 4f < 0.01) {
          //  stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
            tvRatingBar.setText("Your Rating: " + ratingBar.getRating());

        } else if (rating - 5f < 0.01) {
           // stars.getDrawable(2).setColorFilter(getResources().getColor(android.R.color.holo_green_light), PorterDuff.Mode.SRC_ATOP);
            tvRatingBar.setText("Your Rating: " + ratingBar.getRating());
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
