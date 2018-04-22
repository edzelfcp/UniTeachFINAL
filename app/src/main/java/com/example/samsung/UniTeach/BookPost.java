package com.example.samsung.UniTeach;

public class BookPost extends BookPostId{

    public String description, edition, price, image_url, image_thumb;

    public BookPost(){}

    public BookPost (String description, String edition, String price, String image_url, String image_thumb){
        this.description = description;
        this.edition = edition;
        this.price = price;
        this.image_url = image_url;
        this.image_thumb = image_thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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


}
