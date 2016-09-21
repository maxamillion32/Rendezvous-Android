package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import bolts.Task;


public class UserProfile extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button followings, followers, ongoings, verified, pastEvents, follow;
    EventListAdapter3 adapter;
    Calendar c = Calendar.getInstance();
    ParseImageView profilePhoto;
    ListView eventList;
    RatingBar rating;
    TextView eventsBy;
    User user;
    int count = -1;
    String userId;
    final int REQUEST_CAMERA = 1, SELECT_FILE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followers = (Button) findViewById(R.id.button6);
        followers.setOnClickListener(this);
        followings = (Button) findViewById(R.id.button5);
        followings.setOnClickListener(this);
        follow = (Button) findViewById(R.id.button);
        follow.setOnClickListener(this);
        ongoings = (Button) findViewById(R.id.button7);
        ongoings.setOnClickListener(this);
        verified = (Button) findViewById(R.id.button8);
        verified.setOnClickListener(this);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        pastEvents = (Button) findViewById(R.id.button2);
        pastEvents.setOnClickListener(this);
        eventsBy = (TextView) findViewById(R.id.text1);
        profilePhoto = (ParseImageView) findViewById(R.id.image);
        profilePhoto.setOnClickListener(this);

        adapter = new EventListAdapter3(this);
        eventList = (ListView) findViewById(R.id.listView);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(this);

        userId = getIntent().getStringExtra("userId");
        user = new User(userId, this);

        followings.setText( user.getFollowings().size() + " followings");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("followings", user.getParseID());
        try {
            count = query.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        followers.setText( count + "followers" );

        rating.setActivated(false);
        //rating.setEnabled(false);
        rating.setRating( Float.parseFloat( user.getRating() + "") );

        if (!ParseUser.getCurrentUser().getObjectId().equals(userId)){
            profilePhoto.setClickable(false);
            verified.setClickable(false);
            if (ParseUser.getCurrentUser().getList("followings").contains(user.getParseID()))
                follow.setText("unfollow");
            else
                follow.setText("follow");
        }
        else
            follow.setEnabled(false);

        ParseQuery query2 = ParseQuery.getQuery( "Events");
        query2.whereEqualTo( "subscribers", user.getParseID() );
        query2.whereGreaterThan("date", Calendar.getInstance().get( Calendar.YEAR) * 10000 + (Calendar.getInstance().get( Calendar.MONTH) + 1) * 100
                + Calendar.getInstance().get( Calendar.DAY_OF_MONTH));
        try {
            count = query2.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ongoings.setText( count + " ongoings");

        eventsBy.setText("Events by " + user.getUsername());
        profilePhoto.setImageDrawable(user.getProfilePhoto());

        if( user.isVerified()) {
            verified.setText("Verified");
            verified.setEnabled(false);
        }
        else if( user.isRequestedVerify() ) {
            verified.setText( "Requested" );
            verified.setEnabled(false);
        }
        else
            verified.setText("Verify");
    }


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
            if( this.getIntent().getStringExtra("userId").equals(ParseUser.getCurrentUser().getObjectId())){
                Toast.makeText(this, "You are already on your profile", Toast.LENGTH_SHORT).show();
                return true;
            }
            else{
                Intent userProfile = new Intent( this, UserProfile.class);
                userProfile.putExtra("userId", ParseUser.getCurrentUser().getObjectId());
                startActivity( userProfile);
                return true;
            }
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
        if( v == followers){
            Intent intent = new Intent(this, UserList.class);
            //intent.putExtra("key", "followings");
            //intent.putExtra("value", user.getParseID());

            final ArrayList<String> ids = new ArrayList<String>();
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("followings", user.getParseID() );
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, ParseException e) {
                    for( int i = 0; i < parseUsers.size(); i++ ){
                        ids.add(parseUsers.get(i).getObjectId());
                    }
                }
            });
            intent.putExtra("key", "followings");
            intent.putExtra("value", user.getParseID());
            intent.putExtra("ids", ids);
            startActivity(intent);
        }
        else if( v == followings){

            Intent intent = new Intent(this, UserList.class);
            intent.putExtra("ids", (ArrayList)ParseUser.getCurrentUser().getList("followings"));
            //intent.putExtra("key", "followers");
            //intent.putExtra("value", user.getParseID());
            startActivity(intent);
        }
        else if( v == follow){
            if( follow.getText().equals("follow")) {
                List<String> following = ParseUser.getCurrentUser().getList("followings");
                following.add(user.getParseID());
                ParseUser.getCurrentUser().put("followings", following);
                ParseUser.getCurrentUser().saveInBackground();
                follow.setText("unfollow");

            }
            else{
                List<String> following = ParseUser.getCurrentUser().getList("followings");
                following.remove(user.getParseID());
                ParseUser.getCurrentUser().put("followings", following);
                ParseUser.getCurrentUser().saveInBackground();
                follow.setText("follow");

            }
        }
        else if( v == ongoings){
            Intent intent = new Intent(this, EventList.class);
            intent.putExtra("key", "subscribers" );
            intent.putExtra("value",user.getParseID() );
            intent.putExtra( "sortDate", true);
            intent.putExtra("end", 99999999 );
            intent.putExtra( "start", Calendar.getInstance().get( Calendar.YEAR) * 10000 + (Calendar.getInstance().get( Calendar.MONTH) + 1) * 100
                    + Calendar.getInstance().get( Calendar.DAY_OF_MONTH));
            startActivity( intent );
        }
        else if( v == pastEvents ){
            Intent intent = new Intent(this, EventList.class);
            intent.putExtra("key", "organizer");
            intent.putExtra("value", user.getParseID());
            intent.putExtra( "sortDate", true);
            intent.putExtra("start", 0 );
            intent.putExtra( "end", Calendar.getInstance().get( Calendar.YEAR) * 10000 + (Calendar.getInstance().get( Calendar.MONTH) + 1) * 100
                    + Calendar.getInstance().get( Calendar.DAY_OF_MONTH));
            startActivity( intent );
        }
        else if( v == verified ){
            ParseUser.getCurrentUser().put("verifyRequest", true );
            ParseUser.getCurrentUser().saveInBackground();
            verified.setText("Requested");
            verified.setEnabled(false);
        }
        else if ( v == profilePhoto ){
            selectImageOption();
        }
    }
    private void selectImageOption()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (items[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                else if (items[item].equals("Choose from Library"))
                {
                   /* Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_FILE);
                    */
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

     public Bitmap fixOrientation( Bitmap mBitmap)
    {
        if (mBitmap.getWidth() > mBitmap.getHeight())
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            return Bitmap.createBitmap(mBitmap , 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        }
        return mBitmap;
    }

    private void onCaptureImageResult(Intent data)
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = fixOrientation( thumbnail);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try
        {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), thumbnail);
        profilePhoto.setImageDrawable(bitmapDrawable);

        Bitmap btm = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] data2 = stream.toByteArray();
        final ParseFile file = new ParseFile(data2);
        ParseUser.getCurrentUser().put("profilePhoto", file);
        ParseUser.getCurrentUser().saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("organizerID", ParseUser.getCurrentUser().getObjectId() );
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                for (int i = 0; i < parseObjects.size(); i++) {
                    parseObjects.get(i).put("organizerPhoto", file);
                    parseObjects.get(i).saveInBackground();
                }
            }
        });
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data)
    {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        bm = fixOrientation( bm);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] data2 = stream.toByteArray();
        final ParseFile file = new ParseFile(data2);
        ParseUser.getCurrentUser().put("profilePhoto", file);
        ParseUser.getCurrentUser().saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        query.whereEqualTo("organizerID", ParseUser.getCurrentUser().getObjectId() );
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                for(int i = 0; i < parseObjects.size(); i++ ){
                    parseObjects.get( i ).put("organizerPhoto", file);
                    parseObjects.get(i).saveInBackground();
                }
            }
        });

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bm);
        profilePhoto.setImageDrawable(bitmapDrawable);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject event = adapter.getItem(position);
        Log.e( "dsa", ""+event.getInt("date"));
        if( event.getInt("date") >= c.get( Calendar.YEAR) * 10000 + ( c.get( Calendar.MONTH) + 1 ) * 100 + c.get( Calendar.DAY_OF_MONTH)) {
            Intent eventDisplay = new Intent(this, EventDisplay.class);
            eventDisplay.putExtra("category", event.getString("category"));
            eventDisplay.putExtra("eventId", event.getObjectId());
            eventDisplay.putExtra("organizerId", event.getString("organizerID"));
            startActivity(eventDisplay);
        }

        else {
            Intent eventDisplay = new Intent(this, EventDisplayEnded.class );
            eventDisplay.putExtra("category", event.getString("category"));
            eventDisplay.putExtra("eventId", event.getObjectId() );
            startActivity( eventDisplay );
        }
    }

    public class EventListAdapter3 extends ParseQueryAdapter {

        public EventListAdapter3(Context context) {
            super(context, new QueryFactory<ParseObject>() {
                @Override
                public ParseQuery<ParseObject> create() {
                    ParseQuery query = ParseQuery.getQuery("Events");
                    query.whereGreaterThanOrEqualTo("date", Calendar.getInstance().get( Calendar.YEAR) * 10000 + (Calendar.getInstance().get( Calendar.MONTH) + 1) * 100
                            + Calendar.getInstance().get( Calendar.DAY_OF_MONTH) );
                    return query.whereEqualTo("organizerID", ParseUser.getCurrentUser().getObjectId());
                    //.whereGreaterThan("date", c.get( Calendar.YEAR) * 10000 + ( c.get( Calendar.MONTH) + 1 ) * 100 + c.get( Calendar.DAY_OF_MONTH)  )
                }
            });
        }

        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.event_row_layout, null);
            }

            super.getItemView(object, v, parent);

            // Add and download the image

            ParseImageView eventImage =(ParseImageView) v.findViewById(R.id.eventImage);
            eventImage.setPlaceholder( UserProfile.this.getResources().getDrawable(R.drawable.cinema));

            ParseFile imageFile = object.getParseFile("eventImage");
            if (imageFile != null) {
                //eventImage.setParseFile(imageFile);

                byte[] b = null;
                try {
                    b = imageFile.getData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                BitmapDrawable image;
                image = new BitmapDrawable( getApplicationContext().getResources(), bmp);
                eventImage.setImageDrawable( image);
                //image.loadInBackground();
            }

            TextView name = (TextView) v.findViewById(R.id.eventName);
            name.setText(object.getString("name"));

            TextView organizer = (TextView) v.findViewById(R.id.organizerName);
            organizer.setText(object.getString("organizerName"));

            TextView date = (TextView) v.findViewById(R.id.date);
            date.setText(object.getInt("date") % 100 + "/" + (object.getInt("date") % 10000 ) / 100  + "/"
                    + object.getInt("date") / 10000);

            TextView place = (TextView) v.findViewById(R.id.place);
            place.setText(object.getString("city"));

            return v;
        }

        @Override
        public ParseObject getItem(int index) {
            return super.getItem(index);
        }
    }
}
