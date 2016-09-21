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
public class Seminar extends OtherEvent {

    private String speaker;
    private ParseQuery<ParseObject> query = ParseQuery.getQuery( "Events");
    Task<ParseObject> a;

    public Seminar(String parseID, Context context) {
        super( parseID, context );

        a = query.getInBackground( parseID );
        while( !a.isCompleted());

        speaker = a.getResult().getString("speaker");

    }

    public String getSpeaker(){
        return speaker;
    }

    public void setLecturer( final String speaker ){
        this.speaker = speaker;

        query.getInBackground( getParseID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("speaker", speaker);
                    parseObject.saveInBackground();
                }
            }
        });
    }
}
