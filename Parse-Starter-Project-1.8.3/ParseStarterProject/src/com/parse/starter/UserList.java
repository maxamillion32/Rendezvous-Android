package com.parse.starter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UserList extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView userList;
    UserListAdapter adapter;
    String key;
    String value;
    ArrayList<String> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userList = (ListView) findViewById(R.id.listView);


        key = getIntent().getStringExtra("key");
        value = getIntent().getStringExtra("value");
        ids = getIntent().getStringArrayListExtra("ids");

        adapter = new UserListAdapter(this);

        userList.setAdapter(adapter);

        userList.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseUser a = (ParseUser) adapter.getItem(position);
        Intent intent = new Intent(this, UserProfile.class);
        intent.putExtra("userId", a.getObjectId());
        startActivity(intent);
    }

    public class UserListAdapter extends ParseQueryAdapter{

        public UserListAdapter(Context context) {
            super(context,new QueryFactory<ParseUser>() {

                @Override
                public ParseQuery<ParseUser> create() {
                    if( key != null){
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query = query.whereEqualTo(UserList.this.key, UserList.this.value);
                        try {
                            if( query.count() == 0)
                                Toast.makeText(UserList.this, "This list has no items", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return query.whereEqualTo( UserList.this.key, UserList.this.value );
                    }
                    else {
                        List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
                        ParseQuery<ParseUser> query2 = ParseUser.getQuery();
                        query2 = query2.whereEqualTo("objectId", "");
                        queries.add(query2);

                        for (int i = 0; i < ids.size(); i++) {
                            ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query = query.whereEqualTo("objectId", ids.get(i));
                            queries.add(query);
                        }
                        ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
                        try {
                            if( mainQuery.count() == 0)
                                Toast.makeText(UserList.this, "This list has no items", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        QueryFactory<ParseUser> factory = new QueryFactory<ParseUser>() {
                            @Override
                            public ParseQuery<ParseUser> create() {
                                return null;
                            }
                        };
                        return mainQuery;
                    }
                    //
                }
            });
        }

        @Override
        public View getItemView(ParseObject user, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.user_row_layout, null);
            }
            super.getItemView(user, v, parent);

            // Add and download the image

            ParseImageView userImage =(ParseImageView) v.findViewById(R.id.userImage);
            userImage.setPlaceholder( UserList.this.getResources().getDrawable(R.drawable.my_profile));

            ParseFile imageFile = user.getParseFile("profilePhoto");
            if (imageFile != null) {
                userImage.setParseFile(imageFile);
                userImage.loadInBackground();
            }

            TextView username = (TextView) v.findViewById(R.id.usernameText);
            username.setText(user.getString("username"));

            return v;
        }
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