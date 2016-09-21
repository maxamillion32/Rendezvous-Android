package com.parse.starter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Talha Uyar on 23.4.2015.
 */
public class EventListAdapter extends ArrayAdapter {

    ArrayList<OtherEvent> list = new ArrayList<OtherEvent>();

    public EventListAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{

        ImageView eventImage;
        TextView name;
        TextView date;
        TextView organizer;
        TextView place;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;
        if( convertView == null ){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.event_row_layout, parent, false );
            handler = new DataHandler();
            handler.eventImage = (ImageView)row.findViewById(R.id.eventImage);
            handler.name = (TextView) row.findViewById(R.id.eventName);
            handler.date = (TextView) row.findViewById(R.id.date);
            handler.organizer = (TextView) row.findViewById(R.id.organizerName);
            handler.place = (TextView) row.findViewById(R.id.place);
            row.setTag(handler);
        }
        else{
            handler = (DataHandler)row.getTag();
        }

        OtherEvent event = list.get( position );

        handler.eventImage.setImageDrawable( event.getEventImage() );
        handler.name.setText( event.getName());
        handler.organizer.setText(event.getOrganizerID());
        handler.date.setText(  event.getDate() % 100 + "/" + (event.getDate() % 10000 ) / 100  + "/"
                    + event.getDate() / 10000 );
        handler.place.setText(event.getAddress());

        return row;
    }

    @Override
    public Object getItem(int position) {
        return list.get( position );
    }

    @Override
    public int getCount() {
        return list.size();
    }


    public void add(OtherEvent object) {
        super.add( object);
        list.add( object );
    }
}