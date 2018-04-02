package com.example.samsung.UniTeach;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class TutorPostId {

    @Exclude
    public String TutorPostId;

    public <T extends TutorPostId> T withId(@NonNull final String id) {
        this.TutorPostId = id;
        return (T) this;
    }
}
