package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bolts.Task;

/**
 * Created by Talha Uyar on 30.4.2015.
 */
public class Concert extends OtherEvent {

    private String artist;
    private ParseQuery<ParseObject> query = ParseQuery.getQuery( "Events");
    Task<ParseObject> a;

    public Concert(String parseID, Context context) {
        super(parseID, context);

        a = query.getInBackground( parseID );
        while( !a.isCompleted());

        Concert.this.artist = a.getResult().getString("artist");
    }

    public String getArtist(){
        return artist;
    }

    public void setArtist( final String artist ){
        this.artist = artist;
        query.getInBackground( getParseID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("artist", artist);
                    parseObject.saveInBackground();
                }
            }
        });
    }
}