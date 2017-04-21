package com.ushaswini.tripplanner;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * AdapterChat
 * 20/04/2017
 */

public class AdapterChat extends ArrayAdapter<MessageDetails> {

    ArrayList<MessageDetails> mData;
    IShareData iShareData;
    Context mContext;

    public AdapterChat(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MessageDetails> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.iShareData = (IShareData) context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MessageDetails message = mData.get(position);
        View view;

        if(message.getPost_type()) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_image_message, parent, false);
            TextView tv_details = (TextView) view.findViewById(R.id.time_i);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            ImageButton im_comment = (ImageButton) view.findViewById(R.id.im_comment_i);
            LinearLayout li_comments = (LinearLayout) view.findViewById(R.id.list_comments_i);

            PrettyTime time = new PrettyTime();
            String details = message.getUser_name() + "\n" + time.format(message.getPosted_time());
            tv_details.setText(details);

            Picasso.with(mContext).load(message.getImage_url()).into(image);

            ArrayList<Comment> comments = message.getComments();

            if (comments != null) {
                for (Comment comment : comments) {

                    View comment_view = inflater.inflate(R.layout.custom_comment, parent, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    TextView tv_message = (TextView) comment_view.findViewById(R.id.tv_message_c);
                    TextView tv_time = (TextView) comment_view.findViewById(R.id.tv_time_c);

                    String details_c = comment.getName() + "\n" + time.format(comment.getComment_time());
                    tv_message.setText(comment.getText());
                    tv_time.setText(details_c);

                    li_comments.addView(comment_view);

                }

            }

            im_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iShareData.postComment(position);
                }
            });


        }else{
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_message,parent,false);

            TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
            TextView tv_time = (TextView) view.findViewById(R.id.time_m);
            ImageButton im_comment = (ImageButton) view.findViewById(R.id.im_comment_m);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.list_comments);

            tv_message.setText(message.getText());
            PrettyTime time = new PrettyTime();

            String details = message.getUser_name() + "\n" + time.format(message.getPosted_time());
            tv_time.setText(details);

            im_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iShareData.postComment(position);
                }
            });

            ArrayList<Comment> comments = message.getComments();

            if (comments != null) {
                for (Comment comment : comments) {

                    View comment_view = inflater.inflate(R.layout.custom_comment, parent, false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    TextView tv_comment = (TextView) comment_view.findViewById(R.id.tv_message_c);
                    TextView tv_time_c = (TextView) comment_view.findViewById(R.id.tv_time_c);

                    String details_c = comment.getName() + "\n" + time.format(comment.getComment_time());
                    tv_comment.setText(comment.getText());
                    tv_time_c.setText(details_c);

                    linearLayout.addView(comment_view);

                }

            }
        }



        return view;

    }

    interface  IShareData{
        void postComment(int position);

        void deleteImageMessage(MessageDetails messageDetails);

    }
}
