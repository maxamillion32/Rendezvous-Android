package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import bolts.Task;

/**
 * Created by Burak M on 8.5.2015.
 */
public class EditEvent extends ActionBarActivity implements View.OnClickListener{
    private EditText event_name, director, artist, dj, speaker, address, ticket, description, quota, video;
    private CheckBox cbMeal, cbAlcohol;
    private Intent eventInfo;
    private TextView time,date;
    private Button camera,done;
    private ImageView pic;
    private ImageButton map;
    Spinner sCity;
    private static final int DIALOG_ID_TIME = 0;
    private static final int DIALOG_ID_DATE = 1;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private boolean isMapLongClicked = false;
    private double lon;
    private double lat;
    private int hour,min,year,month,day,timeInCloud;
    private String s_event_name, s_address, s_ticket,s_description,s_video, s_person;
    int s_quota = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventInfo = getIntent();
        super.onCreate(savedInstanceState);

        if (eventInfo.getStringExtra("category").equals("Cinema")) {
            setContentView(R.layout.activity_cinema);
            director = (EditText) findViewById(R.id.director);
            director.setText(eventInfo.getStringExtra("tvPersonValue"));
            s_person = director.getText().toString();
        } else if (eventInfo.getStringExtra("category").equals("Theatre")) {
            setContentView(R.layout.activity_theatre);
            director = (EditText) findViewById(R.id.director);
            director.setText(eventInfo.getStringExtra("tvPersonValue"));
            s_person = director.getText().toString();
        } else if (eventInfo.getStringExtra("category").equals("Concert")) {
            setContentView(R.layout.activity_concert);
            artist = (EditText) findViewById(R.id.artist);
            artist.setText(eventInfo.getStringExtra("tvPersonValue"));
            s_person = artist.getText().toString();
        } else if (eventInfo.getStringExtra("category").equals("Seminar")) {
            setContentView(R.layout.activity_seminar);
            speaker = (EditText) findViewById(R.id.speaker);
            speaker.setText(eventInfo.getStringExtra("tvPersonValue"));
            s_person = speaker.getText().toString();
        } else if (eventInfo.getStringExtra("category").equals("Party")) {
            setContentView(R.layout.activity_party);
            dj = (EditText) findViewById(R.id.dj);
            cbMeal = (CheckBox) findViewById( R.id.meal_check);
            cbAlcohol = (CheckBox) findViewById( R.id.alcohol_check);
            dj.setText(eventInfo.getStringExtra("tvPersonValue"));
            s_person = dj.getText().toString();

        } else if (eventInfo.getStringExtra("category").equals("Others")) {
            setContentView(R.layout.activity_other);
            s_person = "none";
        }
        initialise_variables();
        camera.setOnClickListener(this);
        done.setOnClickListener( this);
        time.setOnClickListener( this);
        date.setOnClickListener( this);
        map.setOnClickListener( this);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
        // Retrieve the object by id
        query.getInBackground(eventInfo.getStringExtra("eventId"), new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (e == null) {
                    ParseFile image = (ParseFile) event.get("eventImage");
                    byte[] b = null;
                    try {
                        b = image.getData();
                    } catch (com.parse.ParseException l) {
                        l.printStackTrace();
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                    pic.setImageDrawable(new BitmapDrawable( getApplicationContext().getResources(), bmp));

                    year = event.getInt( "date") / 10000;
                    month = (event.getInt( "date") / 100) % 100;
                    day = event.getInt( "date") % 100;
                    lon = event.getDouble( "longitude");
                    lat = event.getDouble( "latitude");
                    event_name.setText(event.getString("name"));
                    sCity.setSelection(getCityPosition( event.getString( "city")));
                    address.setText(event.getString("address"));
                    ticket.setText(event.getString("ticketLink"));
                    description.setText(event.getString("description"));
                    quota.setText("" + event.getInt("quota"));
                    video.setText(event.getString("videoLink"));
                    date.setText( "Date:  \t" + event.getInt("date") % 100 + "/" + (event.getInt("date") % 10000 ) / 100  + "/"
                            + event.getInt("date") / 10000 );
                    if (eventInfo.getStringExtra("category").equals("Party"))
                    {
                        cbMeal.setChecked(event.getBoolean("hasMeal"));
                        cbAlcohol.setChecked(event.getBoolean("hasAlcohol"));
                    }
                    timeInCloud = event.getInt( "time");
                    hour = timeInCloud / 100;
                    min = timeInCloud % 100;
                    if( hour <= 9)
                    {
                        if( min <= 9)
                            time.setText("Time: \t\t" + "0" + hour  + ":0" + min);
                        else
                            time.setText("Time: \t\t" + "0" + hour  + ":" + min);
                    }
                    else
                    {
                        if( min <= 9)
                            time.setText("Time: \t\t" + hour  + ":0" + min);
                        else
                            time.setText("Time: \t\t" + hour  + ":" + min);
                    }
                }
                else
                    Toast.makeText( EditEvent.this, "Couldn't get previous data. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initialise_variables() {
        event_name = (EditText) findViewById(R.id.event_name);
        sCity = (Spinner) findViewById(R.id.sCity);
        address = (EditText) findViewById(R.id.address);
        ticket = (EditText) findViewById(R.id.ticket);
        video = (EditText) findViewById(R.id.video);
        description = (EditText) findViewById(R.id.description);
        quota = (EditText) findViewById(R.id.quota_no);
        time = (TextView) findViewById(R.id.time);
        date = (TextView) findViewById(R.id.date);
        camera = (Button) findViewById(R.id.camera);
        done = (Button) findViewById(R.id.done);
        map = (ImageButton) findViewById(R.id.map);
        pic = (ImageView) findViewById(R.id.pic);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v)
    {
        if( v == date)
            showDialog(DIALOG_ID_DATE);
        else if( v == time)
            showDialog(DIALOG_ID_TIME);
        else if( v == camera)
            selectImageOption();
        else if( v == map)
        {
            Intent map = new Intent(this, Maps.class);
            map.putExtra("latitude", lat);
            map.putExtra("longitude", lon);
            map.putExtra("listener", true);
            startActivityForResult(map, 2);
        }
        else if( v == done)
        {
            s_event_name = event_name.getText().toString();
            s_address = address.getText().toString();
            s_ticket = ticket.getText().toString();
            s_description = description.getText().toString();
            if( !quota.getText().toString().equals( ""))
                s_quota = Integer.parseInt( quota.getText().toString());
            s_video = video.getText().toString();

            if( s_event_name.equals("") || s_person.equals("") || s_address.equals("") || s_quota == -1 || quota.getText().toString().equals( "")
                    || s_description.equals(""))
            {
                Toast.makeText(this, "Please complete all necessary fields", Toast.LENGTH_SHORT).show();
            }
            else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                // Retrieve the object by id
                query.getInBackground(eventInfo.getStringExtra("eventId"), new GetCallback<ParseObject>() {
                    public void done(ParseObject event, ParseException e) {
                        if (e == null) {
                            event.put("name", event_name.getText().toString());
                            event.put("address", address.getText().toString());
                            event.put("ticketLink", ticket.getText().toString());
                            event.put("description", description.getText().toString());
                            event.put("quota", Integer.parseInt(quota.getText().toString()));
                            event.put("videoLink", video.getText().toString());
                            event.put("time", hour * 100 + min);
                            event.put("city", sCity.getSelectedItem());
                            event.put("date", year * 10000 + month * 100 + day);
                            event.put("isMapClicked", isMapLongClicked);
                            event.put("longitude", lon);
                            event.put("latitude", lat);

                            Bitmap btm = ((BitmapDrawable) pic.getDrawable()).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            btm.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                            byte[] data = stream.toByteArray();
                            ParseFile file = new ParseFile(data);
                            event.put("eventImage", file);

                            if (eventInfo.getStringExtra("category").equals("Cinema"))
                                event.put("director", director.getText().toString());
                            else if (eventInfo.getStringExtra("category").equals("Theatre"))
                                event.put("director", director.getText().toString());
                            else if (eventInfo.getStringExtra("category").equals("Concert"))
                                event.put("artist", artist.getText().toString());
                            else if (eventInfo.getStringExtra("category").equals("Seminar"))
                                event.put("speaker", speaker.getText().toString());
                            else if (eventInfo.getStringExtra("category").equals("Party")) {
                                event.put("dj", dj.getText().toString());
                                event.put("hasAlcohol", cbAlcohol.isChecked());
                                event.put("hasMeal", cbMeal.isChecked());
                            }
                            event.saveInBackground();
                            Toast.makeText(EditEvent.this, "Successfully updated", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(EditEvent.this, "Couldn't update. " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                finish();
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == DIALOG_ID_TIME)
        {
            return new TimePickerDialog(EditEvent.this,TimePickerListener,hour,min,true);
        }

        else if(id == DIALOG_ID_DATE)
        {
            return new DatePickerDialog(EditEvent.this,DatePickerListener,year,month,day);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener TimePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            hour = hourOfDay;
            min = minute;
            int length_hour = String.valueOf(hour).length();
            int length_min = String.valueOf(min).length();
            if((length_hour == 1) && (length_min == 1))
                time.setText("Time:" + "0" + hour + ":" + "0" + min);
            else if(length_hour == 1)
                time.setText("Time:" + "0" + hour + ":" + min);
            else if(length_min == 1)
                time.setText("Time:" + hour + ":" + "0" + min);
            else
                time.setText("Time:" + hour + ":" +  min);
        }
    };

    private DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int input_year,int monthOfYear, int dayOfYear)
        {
            year = input_year;
            month = monthOfYear;
            day = dayOfYear;
            date.setText("Date: " + day + "/" + month + "/" + year);
        }
    };

    private void selectImageOption()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditEvent.this);
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
            else if( requestCode == 2) {
                lat = data.getDoubleExtra( "lat", 0);
                lon = data.getDoubleExtra("lon", 0);
                onAddressBack(data);
                isMapLongClicked = data.getBooleanExtra("isMapClicked", false);
            }
        }
    }

    private void onAddressBack(Intent data)
    {
        String place = data.getStringExtra( "ADDRESS");
        Log.e("place", place);
        address.setText( place);
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
        pic.setImageDrawable(bitmapDrawable);
        camera.setText("Change Picture");
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
        bm = fixOrientation(bm);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bm);
        pic.setImageDrawable(bitmapDrawable);
        camera.setText("Change Picture");
    }

    public int getCityPosition( String city)
    {
        if( city.equals( "Ankara"))
            return 0;
        else if( city.equals( "İstanbul"))
            return 1;
        else if( city.equals( "İzmir"))
            return 2;
        else if( city.equals( "Bursa"))
            return 3;
        else if( city.equals( "Kütahya"))
            return 4;
        return 0;
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
