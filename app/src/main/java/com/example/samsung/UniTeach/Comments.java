package com.example.samsung.UniTeach;

import java.util.Date;

public class Comments {

    private String message;
    private String user_id;


    private String image_url;
    private String image_thumb;
    private Date timestamp;

    public Comments(){

    }

    public Comments(String message, String user_id, Date timestamp, String image_url, String image_thumb) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.image_url = image_url;
        this.image_thumb = image_thumb;
    }

    /*public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }*/



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
