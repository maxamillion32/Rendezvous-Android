package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class CommentDisplay extends ActionBarActivity implements View.OnClickListener {
    String commentId;
    ImageView commentatorPhoto;
    TextView username;
    RatingBar rating;
    TextView comments;
    Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_display);

        Intent intent = getIntent();
        commentId = intent.getStringExtra("commentId");

        commentatorPhoto = (ImageView) findViewById(R.id.commentProfilePic);
        username = (TextView) findViewById(R.id.tvUsername);
        rating = (RatingBar) findViewById(R.id.ratingBar2);
        comments = (TextView) findViewById(R.id.tvComments);

        comment = new Comment(commentId, this);

        commentatorPhoto.setImageDrawable( comment.getCommentatorPhoto());
        commentatorPhoto.setOnClickListener(this);
        username.setText(comment.getCommentatorName());
        username.setOnClickListener(this);

        rating.setStepSize( 0.5f );
        rating.setClickable(false);
        rating.setRating(Float.parseFloat( comment.getRating() + ""));
        // rating.setEnabled(false);

        comments.setText( comment.getCommentText() );
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

    @Override
    public void onClick(View v) {
        if( v == username || v == commentatorPhoto ){
            Intent openProfile = new Intent(this, UserProfile.class);
            openProfile.putExtra("userId", comment.getCommentatorId() );
            startActivity(openProfile);
        }
    }

}
