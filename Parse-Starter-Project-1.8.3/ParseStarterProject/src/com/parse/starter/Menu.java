package com.parse.starter;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import bolts.Task;

/**
 * Created by Burak M on 17.3.2015.
 */
public class Menu extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private ListView categories;
    static int start;
    static int end;
    private ImageButton add;
    boolean checked1, checked2;
    private final String[] LIST_ITEMS = { "Cinema", "Theatre", "Concert", "Seminar", "Party", "Others" };
    private final Integer[] IMAGE_ID = { R.drawable.cinema, R.drawable.theatre, R.drawable.concert, R.drawable.seminar,
                                            R.drawable.party, R.drawable.plus};



	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);

        if( ParseUser.getCurrentUser().get("name") == null ) {
            ParseUser.getCurrentUser().put("name", "Name");
        }
        if( ParseUser.getCurrentUser().get("hasEmail") == null ) {
            ParseUser.getCurrentUser().put("hasEmail", false);
        }
        if( ParseUser.getCurrentUser().get("hasPush") == null ) {
            ParseUser.getCurrentUser().put("hasPush", false);
        }
        /*if( ParseUser.getCurrentUser().get("surname") == null ) {
            ParseUser.getCurrentUser().put("name", "Surname");
        }*/
        if( ParseUser.getCurrentUser().get("profilePhoto") == null ){
            Bitmap btm = ((BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.my_profile)).getBitmap();
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            btm.compress(Bitmap.CompressFormat.JPEG, 20, stream2);
            byte[] data2 = stream2.toByteArray();
            ParseFile file2 = new ParseFile(data2);
            ParseUser.getCurrentUser().put("profilePhoto", file2);
        }
        if( ParseUser.getCurrentUser().get("verifyRequest") == null ) {
            ParseUser.getCurrentUser().put("verifyRequest", false);
        }
        if( ParseUser.getCurrentUser().get("followings") == null ) {
            ParseUser.getCurrentUser().put("followings", new ArrayList<String>());
        }
        if( ParseUser.getCurrentUser().get("subscribedEvents") == null ) {
            ParseUser.getCurrentUser().put("subscribedEvents", new ArrayList<String>());
        }
        if( ParseUser.getCurrentUser().get("numSubscribe") == null ) {
            ParseUser.getCurrentUser().put("numSubscribe", 0);
        }
        ParseUser.getCurrentUser().saveInBackground();

        // check for log out
        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) // if logged out
        {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        setContentView(R.layout.main);

        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        initialise_variables();

        /*Intent intentFromLogin = getIntent();
        getSharedPreferences("ACCOUNT_CREDENTIALS", MODE_PRIVATE).edit()
                .putString("username", intentFromLogin.getStringExtra("username"))
                .putString("password", intentFromLogin.getStringExtra("password"))
                .commit();*/
        categories.setOnItemClickListener(this);
        add.setOnClickListener(this);

        // for demo only cities!!!
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.city_values, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( checked1 ) {
                    Log.d("spinner pos", position + " ");
                    Intent intent = new Intent(Menu.this, EventList.class);
                    intent.putExtra("key", "city");
                    intent.putExtra("value", ((TextView) view).getText().toString());
                    startActivity(intent);
                }
                checked1 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //for demo only events!!!
        Spinner theSpinner = (Spinner) findViewById(R.id.spinnerEvent);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> theAdapter = ArrayAdapter.createFromResource(this,
                R.array.event_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        theAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        theSpinner.setAdapter(theAdapter);
        theSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if( checked2 ) {
                    Log.d("spinner info", ((TextView) view).getText().toString());
                    Log.d("spinner pos", position + " ");

                    Intent intent = new Intent(Menu.this, EventList.class);
                    intent.putExtra("key", "category");
                    intent.putExtra("value", ((TextView) view).getText().toString());
                    startActivity(intent);
                }
                checked2 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




    }

    protected void initialise_variables()
    {
        categories = (ListView) findViewById( R.id.lvCategories);
        add = (ImageButton) findViewById( R.id.ibAdd);
        CustomListView adapter = new CustomListView( this, LIST_ITEMS, IMAGE_ID);
        categories.setAdapter( adapter);
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
            Toast.makeText( this, "You are already in home page", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String category = LIST_ITEMS[position];

        Intent eventList = new Intent( this, EventList.class );

        eventList.putExtra("key", "category");
        eventList.putExtra("value",category );
        startActivity( eventList );
    }

    @Override
    public void onClick(View v)
    {
        if( v.getId() == add.getId())
        {
            Intent addEvent = new Intent( this, AddEvent.class);
            startActivity( addEvent);
        }
    }

    public void sortDate( View v){
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra("sort", "sortDate");
        intent.putExtra("start", start );
        intent.putExtra("end", end);
        startActivity(intent);
    }

    public void sortPopularity( View v){
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra("key", "popularity");
        intent.putExtra("value", "ahanda");
        startActivity(intent);
    }

    public void showDatePickerDialog(View v) {
        if( v.getId() == R.id.button3 ){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "end");
        }
        else{
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "start");
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if( this.getTag().equals("start")){
                Log.e("Girdi Mi?: ", "evet");
                start = year * 10000 + ( month + 1 ) * 100 + day;
            }
            if( this.getTag().equals("end")){
                Log.e("Girdi Mi?: ", "hem de ne");
                end = year * 10000 + ( month + 1 ) * 100 + day;
            }
            Log.d("Date info", year + " "  + month + " "  +  day  + " ");
        }
    }



}