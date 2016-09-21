package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.Calendar;


public class CommentList extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView commentList;
    CommentListAdapter adapter;
    String value;
    RatingBar ratingBar;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        commentList = (ListView) findViewById(R.id.commentListView);

        key = getIntent().getStringExtra("key");
        value = getIntent().getStringExtra("value");

        adapter = new CommentListAdapter(this);
        commentList.setAdapter(adapter);
        commentList.setFocusable(true);
        commentList.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e("itemClick: ", "Hell Yeah!!!!");
        ParseObject comment = adapter.getItem(position);

        Intent commentDisplay = new Intent(this, CommentDisplay.class);
        commentDisplay.putExtra("commentId", comment.getObjectId());
        startActivity(commentDisplay);
    }

    public class CommentListAdapter extends ParseQueryAdapter {

        public CommentListAdapter(Context context) {
            super(context, new QueryFactory<ParseObject>() {
                @Override
                public ParseQuery<ParseObject> create() {
                    ParseQuery query = ParseQuery.getQuery("Comment");
                    query = query.whereEqualTo(CommentList.this.key, CommentList.this.value);
                    try {
                        if( query.count() == 0)
                            Toast.makeText(CommentList.this, "This list has no items", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return query.whereEqualTo( CommentList.this.key, CommentList.this.value );
                }
            });
        }

        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {

            if (v == null) {
                v = View.inflate(getApplicationContext(), R.layout.comment_row_layout, null);
            }

            super.getItemView(object, v, parent);

            // Add and download the image

            ParseImageView commentImage = (ParseImageView) v.findViewById(R.id.commentImage);
            commentImage.setPlaceholder(CommentList.this.getResources().getDrawable(R.drawable.my_profile));

            ParseFile imageFile = object.getParseFile("commentatorPhoto");
            if (imageFile != null) {
                commentImage.setParseFile(imageFile);

                byte[] b = null;
                try {
                    b = imageFile.getData();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                BitmapDrawable image;
                image = new BitmapDrawable(getApplicationContext().getResources(), bmp);
                commentImage.setImageDrawable(image);
                //image.loadInBackground();
            }
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
            TextView name = (TextView) v.findViewById(R.id.commentatorName);
            name.setText(object.getString("commentatorName"));

            ratingBar.setClickable(false);
            ratingBar.setRating( Float.parseFloat( "" + object.getDouble("rating") ) );
            ratingBar.setFocusable(false);

            return v;
        }

        public ParseObject getItem(int index) {
            return super.getItem(index);
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
