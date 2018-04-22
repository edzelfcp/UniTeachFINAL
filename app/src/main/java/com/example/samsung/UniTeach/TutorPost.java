package com.example.samsung.UniTeach;


public class TutorPost extends TutorPostId {

    public String image_url, image_thumb, tutorName;
    //public Date timestamp;

    public TutorPost(){
    }

    public TutorPost(String tutorName, String image_url, String image_thumb) {
        this.image_url = image_url;
        this.image_thumb = image_thumb;

        this.tutorName = tutorName;
        //this.timestamp = timestamp;
        /*this.tutorSubjects = tutorSubjects;
        this.tutorMail = tutorMail;
        this.tutorUni = tutorUni;
        this.tutorMajor = tutorMajor;*/
    }

    public String getTutorName(){
        return tutorName;
    }

    public void setTutorName(String tutorName){
        this.tutorName = tutorName;
    }

    public String getImage_url() {
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
    }

    /*public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }*/


}
