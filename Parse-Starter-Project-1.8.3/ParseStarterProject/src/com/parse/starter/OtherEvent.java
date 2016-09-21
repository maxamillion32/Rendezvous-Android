package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.io.ByteArrayOutputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bolts.Task;

/**
 * Created by Talha Uyar on 30.4.2015.
 */
public class OtherEvent {

    private ParseQuery<ParseObject> query = ParseQuery.getQuery( "Events" );
    Task<ParseObject> a;
    private String parseID;
    private String name;
    private Context context;
    private String organizerID;
    private String organizerName;
    private ArrayList<String> comments;
    private int date;
    private String address;
    private ArrayList<String> subscribers;
    private int key;
    private BitmapDrawable eventImage;
    private BitmapDrawable organizerImage;
    private String videoLink;
    private String ticketLink;
    private String description;
    private int quota;
    private String category;
    private String city;
    private int time;
    private double rating, latitude, longitude;
    private boolean isMapClicked;


    public OtherEvent( String parseID, final Context context ) {
        this.parseID = parseID;
        a = query.getInBackground( parseID );
        while( !a.isCompleted());
        initialize( context );
    }

    public OtherEvent( Context context ){
        initialize( context );
    }

    public void initialize( Context context ){
        name = a.getResult().getString("name");
        this.context = context;
        comments = (ArrayList) a.getResult().getList("comments");
        date = a.getResult().getInt("date");
        address = a.getResult().getString("address");
        subscribers = (ArrayList) a.getResult().getList("subscribers");
        key = a.getResult().getInt("key");
        videoLink = a.getResult().getString("videoLink");
        ticketLink = a.getResult().getString("ticketLink");
        description = a.getResult().getString("description");
        quota = a.getResult().getInt("quota");
        organizerID = a.getResult().getString("organizerID");
        rating = a.getResult().getDouble( "rating");
        organizerName = a.getResult().getString( "organizerName" );
        category = a.getResult().getString("category");
        city = a.getResult().getString("city");
        time = a.getResult().getInt("time");
        isMapClicked = a.getResult().getBoolean( "isMapClicked");
        latitude = a.getResult().getDouble( "latitude");
        longitude = a.getResult().getDouble( "longitude");

        ParseFile image = (ParseFile) a.getResult().get("eventImage");
        byte[] b = null;
        try {
            b = image.getData();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        eventImage = new BitmapDrawable( context.getResources(), bmp);

        ParseFile image2 = (ParseFile) a.getResult().get("organizerPhoto");
        byte[] b2 = null;
        try {
            b2 = image2.getData();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        Bitmap bmp2 = BitmapFactory.decodeByteArray(b2, 0, b2.length);
        organizerImage = new BitmapDrawable( context.getResources(), bmp2);

    }

    public void setAddress(final String address) {
        this.address = address;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if ( e == null ) {
                    parseObject.put("address", address);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public String getCategory(){
        return category;
    }

    public void setComments(final ArrayList<String> comments) {
        this.comments = comments;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("comments", comments);
                    parseObject.saveInBackground();
                }
            }
        });

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDate(final int date) {
        this.date = date;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("date", date);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setDescription(final String description) {
        this.description = description;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("description", description);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setEventImage( BitmapDrawable eventImage) {
        this.eventImage = eventImage;
        Bitmap btm = ( (BitmapDrawable) eventImage ).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        btm.compress( Bitmap.CompressFormat.JPEG, 100, stream );
        byte[] data = stream.toByteArray();

        final ParseFile file = new ParseFile("profilePhoto.jpg", data);
        file.saveInBackground();

        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("eventImage", file);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setKey(final int key) {
        this.key = key;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("key", key);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setName(final String name) {
        this.name = name;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("name", name);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setOrganizerID(final String organizerID) {
        this.organizerID = organizerID;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("organizerID", organizerID);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setQuota(final int quota) {
        this.quota = quota;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("quota", quota);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setSubscribers(final ArrayList<String> subscribers) {
        this.subscribers = subscribers;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("subscribers", subscribers);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setTicketLink(final String ticketLink) {
        this.ticketLink = ticketLink;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("ticketLink", ticketLink);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setVideoLink(final String videoLink) {
        this.videoLink = videoLink;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("videoLink", videoLink);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public ArrayList<Comment> getComments() {

        final ArrayList<Comment> result = new ArrayList<Comment>();

        for( int i = 0; i < comments.size(); i++ ){
            Comment tmp = new Comment( comments.get( i ), this.context );
            result.add( tmp );
        }
        return result;
    }

    public ArrayList<String> getSubscribers() {
        return subscribers;
    }

    public Context getContext() {
        return context;
    }

    public int getDate() {
        return date;
    }

    public Drawable getEventImage() {
        return eventImage;
    }

    public double getLatitude() { return latitude;}

    public double getLongitude() { return  longitude;}

    public int getQuota() {
        return quota;
    }

    public double getRating() { return rating;}

    public boolean isMapClicked() { return isMapClicked; }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public String getOrganizerName(){ return organizerName; }

    public BitmapDrawable getOrganizerImage(){ return organizerImage; }

    public String getTicketLink() {
        return ticketLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getParseID() {
        return parseID;
    }

    public int getTime(){return time;}

    public String getCity(){ return city; }
    public void addComment (Comment comment){
        ArrayList<String> temp = new ArrayList<String>();
        temp = this.comments;
        temp.add( comment.getParseID() );
        this.setComments( temp );
    }

    public void addSubscriber( User user ){
        ArrayList<String> temp = new ArrayList<String>();
        temp = this.subscribers;
        temp.add( user.getParseID() );
        this.setSubscribers( temp );
    }
}