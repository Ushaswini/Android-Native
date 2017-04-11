package com.ushaswini.inclass11;


import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Vinnakota Venkata Ratna Ushaswini
 * MessageDetails
 * 10/04/2017
 */
class Comment{
    String name;
    String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    Date comment_time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getComment_time() {
        return comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
    }

    /*public Map<String,Object> toMap(){

        HashMap<String,Object> result = new HashMap<>();
        result.put("text",text);
        result.put("name",name);
        result.put("comment_time",comment_time);
        return result;
    }*/

    public Comment(String name, String text, Date comment_time, String id) {
        this.name = name;
        this.text = text;
        this.comment_time = comment_time;
        this.id=id;
    }

    public Comment() {
    }
}
public class MessageDetails {

    String text,user_name,  image_url, id;

    Date posted_time;

    ArrayList<Comment> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPosted_time() {
        return posted_time;
    }

    boolean post_type;

    public void setPosted_time(Date posted_time) {

        this.posted_time = posted_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean getPost_type() {
        return post_type;
    }

    public void setPost_type(boolean post_type) {
        this.post_type = post_type;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void saddComments(Comment comment) {
        if(comments==null)
            comments= new ArrayList<>();

        comments.add(comment);
    }

    public MessageDetails() {
    }

    public Map<String,Object> toMap(){

        //Comment comment = new Comment();

        HashMap<String,Object> result = new HashMap<>();
        result.put("text",text);
        result.put("user_name",user_name);
        result.put("posted_time",posted_time);
        result.put("image_url",image_url);
        result.put("comments",comments);
        result.put("post_type",post_type);
        result.put("id",id);





        return result;
    }

    public MessageDetails(String text, String user_name, Date posted_time, String image_url, boolean post_type,String key) {

        this.text = text;
        this.user_name = user_name;
        this.posted_time = posted_time;
        this.image_url = image_url;
        this.post_type = post_type;
        this.id = key;
    }
}
