package com.ushaswini.inclass11;


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

/**
 * Vinnakota Venkata Ratna Ushaswini
 * MessageAdapter
 * 10/04/2017
 */

public class MessageAdapter extends ArrayAdapter<MessageDetails> {

    ArrayList<MessageDetails> mData;

    IShareData iShareData;

    Context mContext;

    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MessageDetails> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.iShareData = (IShareData) context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final MessageDetails messageDetails = mData.get(position);
        View view;

        if(mData.get(position).getPost_type()){
            //image
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_image_message,parent,false);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_updated = (TextView) view.findViewById(R.id.tv_updated);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ImageButton im_delete = (ImageButton) view.findViewById(R.id.im_delete);
            ImageButton im_comment = (ImageButton) view.findViewById(R.id.im_comment);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linae_image);

            tv_name.setText(messageDetails.getUser_name());
            PrettyTime time = new PrettyTime();
            tv_updated.setText(time.format(messageDetails.getPosted_time()));

            Picasso.with(mContext).load(messageDetails.getImage_url()).into(imageView);

            ArrayList<Comment> comments = messageDetails.getComments();
            if(comments != null) {
                for (Comment comment : comments) {

                    View comment_view = inflater.inflate(R.layout.custom_comment, parent, false);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


                    //TODO Alignment on view


                    TextView tv_title_comment = (TextView) comment_view.findViewById(R.id.tv_comment);
                    TextView tv_name_comment = (TextView) comment_view.findViewById(R.id.tv_cmt_name);
                    TextView tv_updated_time_comment = (TextView) comment_view.findViewById(R.id.tv_cmt_time);

                    tv_title_comment.setText(comment.getText());
                    tv_name_comment.setText(comment.getName());
                    PrettyTime time_comment = new PrettyTime();
                    tv_updated_time_comment.setText(time_comment.format(comment.getComment_time()));

                    linearLayout.addView(comment_view);


                }
            }

            im_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iShareData.deleteImageMessage(messageDetails);
                }
            });

            im_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    iShareData.postComment(messageDetails);

                }
            });


        }else{
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_message,parent,false);

           /* TextView tv_title_comment = (TextView) view.findViewById(R.id.tv_title_msg);
            TextView tv_name_comment = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_updated_time_comment = (TextView) view.findViewById(R.id.tv_updated_time);*/

            TextView tv_title = (TextView) view.findViewById(R.id.tv_title_msg);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_updated_time = (TextView) view.findViewById(R.id.tv_updated_time);
            ImageButton im_comment = (ImageButton) view.findViewById(R.id.imageButton);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearlayout);


            tv_title.setText(messageDetails.getText());
            tv_name.setText(messageDetails.getUser_name());
            PrettyTime time = new PrettyTime();
            tv_updated_time.setText(time.format(messageDetails.getPosted_time()));

            im_comment.setOnClickListener(new View.OnClickListener() {



                @Override
                public void onClick(View v) {

                    iShareData.postComment(messageDetails);


                }
            });

            ArrayList<Comment> comments = messageDetails.getComments();
            if(comments != null){
                for (Comment comment: comments) {

                    View comment_view = inflater.inflate(R.layout.custom_comment,parent,false);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


                    //TODO Alignment on view


                    TextView tv_title_comment = (TextView) comment_view.findViewById(R.id.tv_comment);
                    TextView tv_name_comment = (TextView) comment_view.findViewById(R.id.tv_cmt_name);
                    TextView tv_updated_time_comment = (TextView) comment_view.findViewById(R.id.tv_cmt_time);

                    tv_title_comment.setText(comment.getText());
                    tv_name_comment.setText(comment.getName());
                    PrettyTime time_comment = new PrettyTime();
                    tv_updated_time_comment.setText(time_comment.format(comment.getComment_time()));

                    linearLayout.addView(comment_view);



                }
            }


        }
        return view;
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
