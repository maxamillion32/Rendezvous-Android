package com.parse.starter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventList extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView eventList;
    EventListAdapter2 adapter;
    String value;
    String key;
    boolean checked1, checked2;
    boolean popularitySort, dateSort, ended, ongoing;
    static int start, end;
    ArrayList<String> ids;
    Date search;
    //ArrayList<String> storeIds;
    //ArrayList<OtherEvent> events;
    //EventListAdapter adapter;
    //private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventList = (ListView) findViewById(R.id.listView);

        key = getIntent().getStringExtra("key");
        value = getIntent().getStringExtra("value");

        adapter = new EventListAdapter2(this);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(this);

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
                    Intent intent = new Intent(EventList.this, EventList.class);
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

                    Intent intent = new Intent(EventList.this, EventList.class);
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

    @SuppressWarnings("deprecation")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject event = adapter.getItem(position);
        Calendar c = Calendar.getInstance();
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

    public class EventListAdapter2 extends ParseQueryAdapter {

        public EventListAdapter2(Context context) {
            super(context, new QueryFactory<ParseObject>() {
                @Override
                public ParseQuery<ParseObject> create() {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
                    if( EventList.this.getIntent().getBooleanExtra("sortDate", false) )
                        query = query.whereGreaterThanOrEqualTo("date", EventList.this.getIntent().getIntExtra("start", 0)).whereLessThanOrEqualTo("date", EventList.this.getIntent().getIntExtra("end", 999999999));
                    if( EventList.this.getIntent().getBooleanExtra("sortPopularity", false) )
                        query = query.orderByDescending("rating");
                    if( EventList.this.getIntent().getBooleanExtra("sortCity", false ) )
                        query = query.whereEqualTo("city", EventList.this.getIntent().getStringExtra("city"));
                    if( EventList.this.getIntent().getBooleanExtra("sortCategory", false ) )
                        query = query.whereEqualTo("category",EventList.this.getIntent().getStringExtra("category") );
                    try {
                        if( query.whereEqualTo(EventList.this.key, EventList.this.value).count() == 0)
                            Toast.makeText(EventList.this, "This list has no items", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return query.whereEqualTo(EventList.this.key, EventList.this.value);
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
            eventImage.setPlaceholder( EventList.this.getResources().getDrawable(R.drawable.cinema));

            ParseFile imageFile = object.getParseFile("eventImage");
            if (imageFile != null) {
                //eventImage.setParseFile(imageFile);

                byte[] b = null;
                try {
                    b = imageFile.getData();
                } catch (com.parse.ParseException e) {
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
            place.setText(object.getString("address"));
            return v;
        }

        @Override
        public ParseObject getItem(int index)
        {
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

    public void sortDate( View v){
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra("sort", "sortDate");
        intent.putExtra("start", start );
        intent.putExtra("end", end);
        startActivity(intent);
    }

    public void sortPopularity( View v){
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra("sortPopularity", true);
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