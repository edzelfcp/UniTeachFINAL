package com.example.samsung.UniTeach;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class BookPostId {

    @Exclude
    public String BookPostId;

    public <T extends BookPostId> T withId(@NonNull final String id){
        this.BookPostId = id;
        return (T) this;
    }
}
