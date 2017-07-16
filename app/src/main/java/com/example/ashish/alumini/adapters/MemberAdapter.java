package com.example.ashish.alumini.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ashish on 3/3/16.
 */
public class MemberAdapter  extends RecyclerView.Adapter<MemberAdapter.MyViewHolder>{

    public  String TAG = getClass().getSimpleName();


    List<MemberInstance> mListMembers;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, year, designation, location;
        public CircleImageView mImageView;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textView_name);
            designation = (TextView) view.findViewById(R.id.textView_designation);
            year = (TextView) view.findViewById(R.id.textView_passing_year);
            location = (TextView) view.findViewById(R.id.textView_location);
            mImageView = (CircleImageView) view.findViewById(R.id.profileimage);
        }


    }


    public MemberAdapter(List<MemberInstance> memberList) {
        mListMembers = memberList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MemberInstance aInstance = mListMembers.get(position);
        holder.name.setText(aInstance.getName());
        holder.designation.setText(aInstance.getDesignation());
        holder.year.setText(aInstance.getYear());
        holder.location.setText(aInstance.getWork());
        Picasso.with(holder.mImageView.getContext())
                .load(ApiClient.BASE_URL + "upload/uploads/thumbs/" + aInstance.get_id())
                .placeholder(new IconicsDrawable(holder.mImageView.getContext()).icon(FontAwesome.Icon.faw_user)
                        .color(Color.LTGRAY)
                        .sizeDp(50))
                .error(new IconicsDrawable(holder.mImageView.getContext()).icon(FontAwesome.Icon.faw_user_secret)
                        .color(Color.LTGRAY)
                        .sizeDp(50))
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mListMembers.size();
    }


}
