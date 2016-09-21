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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.*;
import android.webkit.URLUtil;
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

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bolts.Task;


public class AddParty extends ActionBarActivity implements View.OnClickListener
{
    TextView time, date;
    Button camera,done;
    ImageButton map;
    ImageView pic;
    EditText event_name,address,ticket,description,quota,video, dj;
    TextView city;
    Spinner sCity;
    CheckBox alcohol,meal;
    Boolean is_alcohol_checked,is_meal_checked;
    static final int DIALOG_ID_TIME = 0;
    static final int DIALOG_ID_DATE = 1;
    static final int REQUEST_CAMERA = 0;
    static final int SELECT_FILE = 1;

    private boolean isTimeSet = false, check1 = true, check2 = true;
    private boolean isDateSet = false;
    private boolean flag2 = false;
    private double lat, lon;

    int hour,min,year,month,day;
    String s_event_name,s_address,s_ticket,s_description,s_video;
    int s_quota = -1;
    BitmapDrawable eventImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        time = (TextView) findViewById(R.id.time);
        time.setOnClickListener(this);
        date = (TextView) findViewById(R.id.date);
        date.setOnClickListener(this);
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        camera = (Button) findViewById(R.id.camera);
        camera.setOnClickListener(this);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);
        map = (ImageButton) findViewById(R.id.map);
        map.setOnClickListener(this);
        pic = (ImageView) findViewById(R.id.pic);
        event_name = (EditText) findViewById(R.id.event_name);
        address = (EditText) findViewById(R.id.address);
        ticket = (EditText) findViewById(R.id.ticket);
        video = (EditText) findViewById(R.id.video);
        description = (EditText) findViewById(R.id.description);
        quota = (EditText) findViewById(R.id.quota_no);
        alcohol = (CheckBox) findViewById(R.id.alcohol_check);
        alcohol.setOnClickListener(this);
        meal = (CheckBox) findViewById(R.id.meal_check);
        meal.setOnClickListener(this);
        city = (TextView) findViewById(R.id.tvCityTitle);
        sCity = (Spinner) findViewById( R.id.sCity);
        dj = (EditText) findViewById( R.id.dj);
    }
    @Override
    public void onClick(View v)
    {
        if(v == time)
            showDialog(DIALOG_ID_TIME);
        if(v == date)
            showDialog(DIALOG_ID_DATE);
        if(v == camera)
        {
            selectImageOption();
        }
        if(v == done) {
            s_event_name = event_name.getText().toString();
            s_address = address.getText().toString();
            s_ticket = ticket.getText().toString();
            s_description = description.getText().toString();
            if( !quota.getText().toString().equals( ""))
                s_quota = Integer.parseInt(quota.getText().toString());
            s_video = video.getText().toString();
            is_alcohol_checked = alcohol.isChecked();
            is_meal_checked = meal.isChecked();


            boolean check3 = false;
            if ( dj.getText().toString().equals("") ||s_event_name.equals("") || s_address.equals("") || s_quota == -1 ||
                    !isTimeSet || !isDateSet) {
                Toast.makeText(this, "Please complete all necessary fields", Toast.LENGTH_SHORT).show();
            }
            else
                check3 = true;
            if( ( !Patterns.WEB_URL.matcher(s_ticket).matches() || !URLUtil.isValidUrl(s_ticket) ) && !s_ticket.equals("") ){
                Log.e("Ticket: ", "1");
                if ( !s_ticket.startsWith( "http://www." ) && !s_ticket.startsWith("https://www.") ){
                    if( !s_ticket.startsWith( "www." ) && !s_ticket.startsWith("www.") )
                        s_ticket = "http://www." + s_ticket;
                    else
                        s_ticket = "http://" + s_ticket;
                    Log.e("Ticket: ","2 " + s_ticket);
                }
                Log.e("TicketValid: ", Patterns.WEB_URL.matcher(s_ticket).matches() + "" );
                if( !Patterns.WEB_URL.matcher(s_ticket).matches() ) {
                    Log.e("Ticket: ", "3");
                    check1 = false;
                    Toast.makeText(this, "Ticket Link is invalid. Please re-enter the Ticket Link.", Toast.LENGTH_SHORT).show();
                }
                else
                    check1 = true;
            }
            if( ( !Patterns.WEB_URL.matcher(s_video).matches() || !URLUtil.isValidUrl(s_video) ) && !s_video.equals("") ) {
                Log.e("Video: ","1");
                if ( !s_video.startsWith( "http://www." ) && !s_video.startsWith("https://www.") ){
                    if( !s_video.startsWith( "www." ) && !s_video.startsWith("www.") )
                        s_video = "http://www." + s_video;
                    else
                        s_video = "http://" + s_video;
                    Log.e("Video: ","2 " + s_video);
                }
                Log.e("VideoValid: ", Patterns.WEB_URL.matcher(s_video).matches() + "" );
                if( !Patterns.WEB_URL.matcher(s_video).matches() ) {
                    Log.e("Video: ", "3");
                    check2 = false;
                    Toast.makeText(this, "Video Link is invalid. Please re-enter the Video Link.", Toast.LENGTH_SHORT).show();
                }
                else
                    check2 = true;
            }
            if( check1 && check2 && check3)  {

                ParseObject party = new ParseObject("Events");

                party.put( "rating", 0);
                party.put("name", s_event_name);
                party.put("organizerID", ParseUser.getCurrentUser().getObjectId());
                party.put("comments", new ArrayList<String>());
                party.put("date", year * 10000 + month * 100 + day);
                party.put("address", s_address);
                party.put("subscribers", new ArrayList<String>());
                party.put("key", (int) (Math.random() * 1000000));
                party.put("category", "Party");
                party.put("comments", new ArrayList<String>());
                party.put("isMapClicked", flag2);
                party.put("latitude", lat);
                party.put("longitude", lon);

                party.put("rating", 0 );
                party.put("numComment", 0);

                Bitmap btm;
                if (eventImage == null) {
                    btm = BitmapFactory.decodeResource(getResources(), R.drawable.party);
                } else {
                    btm = (eventImage).getBitmap();
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                btm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] data = stream.toByteArray();
                ParseFile file = new ParseFile(data);
                party.put("eventImage", file);

                party.put("videoLink", s_video);
                party.put("ticketLink", s_ticket);
                party.put("description", s_description);
                party.put("city", sCity.getSelectedItem());
                party.put("quota", s_quota);
                party.put("hasAlcohol", is_alcohol_checked );
                party.put("hasMeal", is_meal_checked);
                party.put( "dj", dj.getText().toString());
                party.put("organizerName", ParseUser.getCurrentUser().getString("username"));
                party.put("time", hour * 100 + min);

                if( ParseUser.getCurrentUser().getParseFile("profilePhoto") != null ) {
                    ParseFile file2 = ParseUser.getCurrentUser().getParseFile("profilePhoto");
                    party.put("organizerPhoto", file2);
                }
                else {
                    Bitmap btm2;
                    btm2 = ((BitmapDrawable) getApplicationContext().getResources().getDrawable(R.drawable.my_profile)).getBitmap();
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    btm2.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                    byte[] data2 = stream.toByteArray();
                    ParseFile file2 = new ParseFile(data);
                    ParseUser.getCurrentUser().put("profilePhoto", file2);
                    ParseUser.getCurrentUser().saveInBackground();
                    party.put("organizerPhoto", file2);
                }

                party.saveInBackground();
                Toast.makeText(this, "The event has been added successfully!", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        if(v == map)
        {
            Intent maps = new Intent( this, Maps.class);
            startActivityForResult( maps, 2);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == DIALOG_ID_TIME)
        {
            return new TimePickerDialog(AddParty.this,TimePickerListener,hour,min,true);
        }

        else if(id == DIALOG_ID_DATE)
        {
            return new DatePickerDialog(AddParty.this,DatePickerListener,year,month,day);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener TimePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            isTimeSet = true;
            hour = hourOfDay;
            min = minute;
            int length_hour = String.valueOf(hour).length();
            int length_min = String.valueOf(min).length();
            if((length_hour == 1) && (length_min == 1))
                time.setText("Time:\t" + "0" + hour + ":" + "0" + min);
            else if(length_hour == 1)
                time.setText("Time:\t" + "0" + hour + ":" + min);
            else if(length_min == 1)
                time.setText("Time:\t" + hour + ":" + "0" + min);
            else
                time.setText("Time:\t" + hour + ":" +  min);
        }
    };

    private DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int input_year,int monthOfYear, int dayOfYear)
        {
            isDateSet = true;
            year = input_year;
            month = monthOfYear + 1;
            day = dayOfYear;
            date.setText("Date:\t" + day + "/" + month + "/" + year);
        }
    };

    private void selectImageOption()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddParty.this);
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
                try {
                    onCaptureImageResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if( requestCode == 2) {
                onAddressBack(data);
                flag2 = data.getBooleanExtra("isMapClicked", false);
                lat = data.getDoubleExtra("lat", 0);
                lon = data.getDoubleExtra("lon", 0);
            }
        }
    }

    private void onAddressBack(Intent data)
    {
        String place = data.getStringExtra( "ADDRESS");
        address.setText( place);
        s_address = place;
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

    private void onCaptureImageResult(Intent data) throws IOException
    {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        thumbnail = fixOrientation( thumbnail);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File( Environment.getExternalStorageDirectory(),
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

        eventImage = bitmapDrawable;
        pic.setImageDrawable(bitmapDrawable);
        camera.setText("Change Picture");
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
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bm);
        pic.setImageDrawable(bitmapDrawable);
        camera.setText("Change Picture");
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
