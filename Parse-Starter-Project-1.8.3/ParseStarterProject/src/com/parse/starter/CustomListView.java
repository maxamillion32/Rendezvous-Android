package com.parse.starter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Burak M on 29.3.2015.
 */
public class CustomListView extends ArrayAdapter<String>
{
    private final Activity CONTEXT;
    private final String[] LIST_ITEMS;
    private final Integer[] IMAGE_ID;

    public CustomListView(Activity context, String[] listItems, Integer[] imageId)
    {
        super(context, R.layout.menu_list, listItems);
        CONTEXT = context;
        LIST_ITEMS = listItems;
        IMAGE_ID = imageId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = CONTEXT.getLayoutInflater();
        View rowView = inflater.inflate( R.layout.menu_list, null, true);

        TextView category = (TextView) rowView.findViewById( R.id.category_name);
        ImageView icon = (ImageView) rowView.findViewById( R.id.category_image);

        category.setText( LIST_ITEMS[position]);
        icon.setImageResource( IMAGE_ID[position]);

        return rowView;
    }
}
