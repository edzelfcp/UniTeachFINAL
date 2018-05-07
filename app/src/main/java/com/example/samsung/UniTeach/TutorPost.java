package com.example.samsung.UniTeach;


public class TutorPost extends TutorPostId {

    public String tutorName;
    public String image_url;
    public String image_thumb;

    public String tutorMajor;
    public String tutorUni;
    public String tutorMail;
    //public Date timestamp;

    public String tutorSubjects;

    public String tutorVerified;

    public TutorPost(){
    }



    public TutorPost(String tutorVerified, String tutorName, String image_url, String image_thumb, String tutorMajor, String tutorUni, String tutorMail, String tutorSubjects) {
        this.tutorVerified = tutorVerified;

        this.tutorName = tutorName;
        this.image_url = image_url;
        this.image_thumb = image_thumb;

        this.tutorMajor = tutorMajor;
        this.tutorUni = tutorUni;
        this.tutorMail = tutorMail;

        this.tutorSubjects = tutorSubjects;
    }

    public String getTutorVerified() {
        return tutorVerified;
    }

    public void setTutorVerified(String tutorVerified) {
        this.tutorVerified = tutorVerified;
    }


    public String getTutorSubjects() {
        return tutorSubjects;
    }

    public void setTutorSubjects(String tutorSubjects) {
        this.tutorSubjects = tutorSubjects;
    }

    public String getTutorMajor() {
        return tutorMajor;
    }

    public void setTutorMajor(String tutorMajor) {
        this.tutorMajor = tutorMajor;
    }

    public String getTutorUni() {
        return tutorUni;
    }

    public void setTutorUni(String tutorUni) {
        this.tutorUni = tutorUni;
    }

    public String getTutorMail() {
        return tutorMail;
    }

    public void setTutorMail(String tutorMail) {
        this.tutorMail = tutorMail;
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
