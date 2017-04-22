package com.ushaswini.tripplanner;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * AdapterFriends
 * 21/04/2017
 */

public class AdapterFriends extends ArrayAdapter<User> {

    ArrayList<User> friends;

    Context mContext;

    int mResource;


    IHandleConnect iHandleConnect;


    public AdapterFriends(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.friends = objects;
        this.mResource = resource;
        this.iHandleConnect = (IHandleConnect) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final User friend = friends.get(position);
        ViewHolder_Friend holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.list_row_item,parent,false);
            convertView = inflater.inflate(mResource,parent,false);

            holder = new ViewHolder_Friend();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.im_connect);
            holder.imageView = (ImageView) convertView.findViewById(R.id.im_photo);

            convertView.setTag(holder);
        }

        holder = (ViewHolder_Friend) convertView.getTag();
        TextView title = holder.textView;
        ImageView imageView = holder.imageView;
        final ImageButton imageButton = holder.imageButton;

        String details = friend.getfName() + "," + friend.getlName() + "\n" + friend.getGender();

        title.setText(details);
        Picasso.with(mContext).load(friend.getImageUrl()).into(imageView);


        switch (friend.getStatus()){
            case FRIEND: imageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_friend));
                break;
            case SENT: imageButton.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_sent));
                break;
            case RECEIVED:imageButton.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_receive));
                break;
            case UNCONNECTED: imageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_person_add));
                break;

        }
        /*if(isFriend){
            imageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_friend));
            //TODO show profile
        }else{
            imageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_person_add));
        }*/

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (friend.getStatus()){
                    case FRIEND: {
                        iHandleConnect.removeFriend(friend);
                        break;

                    }
                    case SENT:
                        {
                        iHandleConnect.displaySentMessage();
                        break;

                    }
                    case RECEIVED:{
                        iHandleConnect.displayReceivedMessage(friend);
                        imageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_friend));
                        break;
                    }

                    case UNCONNECTED:
                        {
                            iHandleConnect.addFriend(friend);
                            imageButton.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_sent));
                            break;


                        }


                }
                //change icon
            }
        });

        return convertView;
    }

    interface IHandleConnect{
        void addFriend(User user);
        void displayReceivedMessage(User friend);
        void displaySentMessage();
        void removeFriend(User user);
    }
}
