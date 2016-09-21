package com.parse.starter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import bolts.Task;

/**
 * Created by Talha Uyar on 30.4.2015.
 */
public class Comment {

    private final ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
    private String commentatorName;
    private BitmapDrawable commentatorPhoto;
    private String commentatorId;
    private String commentText;
    private String parseID;
    private double rating;
    private Context context;
    Task<ParseObject> a;

    public Comment( String parseID, Context context ) {

        Comment.this.context = context;
        a = query.getInBackground(parseID);
        while (!a.isCompleted()) ;

        Comment.this.commentText = a.getResult().getString("commentText");
        Comment.this.parseID = a.getResult().getObjectId();
        Comment.this.commentatorName = a.getResult().getString("commentatorName");
        Comment.this.rating = a.getResult().getDouble("rating");
        commentatorId = a.getResult().getString("commentatorID");

        ParseFile image = (ParseFile) a.getResult().get("commentatorPhoto");
        byte[] b = null;
        try {
            b = image.getData();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        commentatorPhoto = new BitmapDrawable( context.getResources(), bmp);
    }

    public void setComment(final String comment) {
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("comment", comment);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public void setRating(final int rating) {
        this.rating = rating;
        query.getInBackground( parseID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    parseObject.put("rating", rating);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public double getRating() {
        return rating;
    }

    public String getCommentatorId(){ return commentatorId; }

    public String getCommentText() {
        return commentText;
    }

    public String getCommentatorName() {
        return commentatorName;
    }

    public String getParseID() {
        return parseID;
    }

    public BitmapDrawable getCommentatorPhoto(){
        return commentatorPhoto;
    }
}
