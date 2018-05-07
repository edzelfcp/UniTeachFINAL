package com.example.samsung.UniTeach;


public class User {

    public String image;
    public String name;


    public String university;

    public User(){}

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String image, String name, String university){
        this.image = image;
        this.name = name;
        this.university = university;
    }

}
