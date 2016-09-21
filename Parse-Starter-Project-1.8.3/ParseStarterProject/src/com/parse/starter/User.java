package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

/**
 * Created by Talha Uyar on 30.4.2015.
 */

public class User{

    private final ParseQuery<ParseUser> query = ParseUser.getQuery();
    private final ParseUser parseUser;
    private String parseID;
    private String username;
    private String name;
    private String surname;
    private String address;
    private String email;
    private int numSubscribe;
    private String phoneNo;
    private BitmapDrawable profilePhoto;
    private ArrayList<String> followers = new ArrayList<String>();
    private ArrayList<String> followings = new ArrayList<String>();
    private ArrayList<String> subscribedEvents = new ArrayList<String>();
    private ArrayList<String> organizedEvents = new ArrayList<String>();
    private boolean verified;
    private boolean requestedVerify;
    private double rating;
    private Context context;

    public User( String parseID, final Context context ){

        this.parseID = parseID;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );

        parseUser = a.getResult().get(0);
        this.context = context;
        name = a.getResult().get(0).getString("name");
        username = a.getResult().get(0).getString("username");
        surname = a.getResult().get(0).getString("surname");
        address = a.getResult().get(0).getString("address");
        email = a.getResult().get(0).getString("email");
        phoneNo = a.getResult().get(0).getString("phone");
        followers = (ArrayList)a.getResult().get(0).getList("followers");
        followings = (ArrayList)a.getResult().get(0).getList("followings");
        subscribedEvents = (ArrayList)a.getResult().get(0).getList("subscribedEvents");
        organizedEvents = (ArrayList)a.getResult().get(0).getList("organizedEvents");
        verified = a.getResult().get(0).getBoolean("verified");
        rating = a.getResult().get(0).getInt("rating");
        numSubscribe = a.getResult().get(0).getInt("numSubscribe");
        requestedVerify = a.getResult().get(0).getBoolean("verifyRequest");

        ParseFile image = (ParseFile) a.getResult().get(0).get("profilePhoto");
        byte[] b = null;
        try {
            b = image.getData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        profilePhoto = new BitmapDrawable( context.getResources(), bmp);

    }

    public void setAddress(final String address) throws ParseException {
        this.address = address;
        query.whereEqualTo("objectId", parseID);
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("address", address);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setName(final String name) {
        this.name = name;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("name", name);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setEmail(final String email) {
        this.email = email;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("email", email);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setPhoneNo(final String phoneNo) {
        this.phoneNo = phoneNo;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("phoneNo", phoneNo);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setFollowers(final ArrayList<String> followers) {
        this.followers = followers;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("followers", followers);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setSurname(final String surname) {
        this.surname = surname;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("surname", surname);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setFollowings(final ArrayList<String> followings) {
        this.followings = followings;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("followings", followings);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(final String username) {
        this.username = username;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("username", username);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;

    }

    public void setProfilePhoto( BitmapDrawable profilePhoto) {
        this.profilePhoto = profilePhoto;

        Bitmap btm = ( (BitmapDrawable) profilePhoto ).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        btm.compress( Bitmap.CompressFormat.JPEG, 100, stream );
        byte[] data = stream.toByteArray();

        final ParseFile file = new ParseFile("profilePhoto.jpg", data);
        file.saveInBackground();

        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("profilePhoto", file);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setOrganizedEvents(final ArrayList<String> organizedEvents) {
        this.organizedEvents = organizedEvents;

        parseUser.put("organizedEvents",organizedEvents);
        parseUser.saveInBackground();

    }

    public void setRating(final double rating) {
        this.rating = rating;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("rating", rating);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void setSubscribedEvents(final ArrayList<String> subscribedEvents) {
        this.subscribedEvents = subscribedEvents;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("subscribedEvents", subscribedEvents);
        a.getResult().get(0).saveInBackground();
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setVerified(final boolean verified) {
        this.verified = verified;
        query.whereEqualTo( "objectId", parseID );
        Task< List<ParseUser> > a = query.findInBackground();
        while ( a.isCompleted() == false );
        a.getResult().get(0).put("verified", verified);
        try {
            a.getResult().get(0).save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public BitmapDrawable getProfilePhoto() {
        return profilePhoto;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public ArrayList<String> getFollowings() {
        return followings;
    }

    public ArrayList<String> getOrganizedEvents() {
        return organizedEvents;
    }

    public ArrayList<String> getSubscribedEvents() {
        return subscribedEvents;
    }

    public boolean isVerified() {
        return verified;
    }

    public Context getContext() {
        return context;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getSurname() {
        return surname;
    }

    public String getParseID() {
        return parseID;
    }

    public int getNumSubscribe(){ return numSubscribe;}

    public boolean isRequestedVerify(){ return requestedVerify;}

    public void follow( User user ){
        ArrayList<String> temp = followings;

        temp.add( user.getParseID() );
        this.setFollowings(temp);

        temp = user.getFollowers();
        temp.add( this.getParseID() );
        user.setFollowers(temp);
    }

    public void unfollow( User user ){

        ArrayList<String> temp = followings;

        temp.remove(user.getParseID());
        this.setFollowings( temp );

        temp = user.getFollowers();
        temp.remove(this.getParseID());
        user.setFollowers( temp );
    }

    public void organize( String storeId ){
        ArrayList<String> temp = organizedEvents;
        temp.add( storeId );
        setOrganizedEvents(temp);
    }

    public void subscribe( String storeId ){
        ArrayList<String> temp = subscribedEvents;
        temp.add( storeId );
        setSubscribedEvents(temp);
    }
}