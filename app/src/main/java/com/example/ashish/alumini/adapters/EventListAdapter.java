package com.example.ashish.alumini.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ashish.alumini.R;
import com.example.ashish.alumini.network.pojo.EventListInstance;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ashish on 11/3/16.
 */
public class EventListAdapter extends ArrayAdapter<EventListInstance> {
    List<EventListInstance> mListEvents;

    @Bind(R.id.imageView_eventPic_list_layout)
    ImageView mImageView;
    @Bind(R.id.TextView_eventName) TextView mTextViewEventName;



    public EventListAdapter(Context context, int resource, List<EventListInstance> objects) {
        super(context, resource, objects);
        mListEvents =objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.list_layout_events, null);
        }
        //Butterknife Injections
        ButterKnife.bind(this,convertView);

        EventListInstance item = getItem(position);

        mTextViewEventName.setText(item.getName());

        Picasso.with(mImageView.getContext())
                .load(item.getImageUrl())
                .placeholder(new IconicsDrawable(mImageView.getContext()).icon(FontAwesome.Icon.faw_file_image_o)
                        .color(Color.LTGRAY)
                        .sizeDp(50))
                .error(new IconicsDrawable(mImageView.getContext()).icon(FontAwesome.Icon.faw_file_image_o)
                        .color(Color.LTGRAY)
                        .sizeDp(50))
                .into(mImageView);

        return convertView;
    }

    @Override
    public EventListInstance getItem(int position) {
        return mListEvents.get(position);
    }

}
