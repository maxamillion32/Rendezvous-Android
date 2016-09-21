package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
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
public class Cinema extends OtherEvent {

    private String directorName;
    private ParseQuery<ParseObject> query = ParseQuery.getQuery( "Events");
    Task<ParseObject> a;

    public Cinema(String parseID, Context context) {
        super( parseID, context );

        a = query.getInBackground( parseID );
        while( !a.isCompleted());
        this.directorName = a.getResult().getString("director");
    }

    public String getDirectorName(){
        return directorName;
    }

    public void setDirectorName( final String directorName ){
        this.directorName = directorName;

        query.getInBackground( getParseID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("directorName", directorName);
                    parseObject.saveInBackground();
                }
            }
        });
    }
}
