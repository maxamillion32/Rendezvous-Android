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
public class Party extends OtherEvent {

    private boolean alcoholAllowed;
    private boolean mealService;
    private String Dj;
    private ParseQuery<ParseObject> query = ParseQuery.getQuery( "Events" );
    Task<ParseObject> a;

    public Party(String parseID, Context context) {
        super( parseID, context );

        a = query.getInBackground( parseID );
        while( !a.isCompleted());

        alcoholAllowed = a.getResult().getBoolean("hasAlcohol");
        mealService = a.getResult().getBoolean("hasMeal");
        Dj = a.getResult().getString("dj");
    }

    public boolean isAlcoholAllowed(){
        return alcoholAllowed;
    }

    public void setAlcoholAllowed( final boolean alcoholAllowed ){
        this.alcoholAllowed = alcoholAllowed;
        query.getInBackground(getParseID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("alcoholAllowed", alcoholAllowed);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public boolean hasMealService(){
        return mealService;
    }

    public void setMealService( final boolean mealService ){
        this.mealService = mealService;
        query.getInBackground( getParseID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("mealService", mealService );
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public String getDJ(){
        return Dj;
    }
}
