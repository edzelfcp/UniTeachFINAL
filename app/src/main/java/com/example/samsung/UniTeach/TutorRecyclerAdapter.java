package com.example.samsung.UniTeach;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorRecyclerAdapter extends RecyclerView.Adapter<TutorRecyclerAdapter.ViewHolder> {

    public List<TutorPost> tutor_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public TutorRecyclerAdapter(List<TutorPost> tutor_list) {

        this.tutor_list = tutor_list;
    }

    @Override
    public TutorRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_list_item, parent, false);
        Context context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new TutorRecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final TutorRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String tutorPostId = tutor_list.get(position).TutorPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = tutor_list.get(position).getDesc();
        //holder.setDescText(desc_data);

        //String image_url = tutor_list.get(position).getImage_url();
        //String thumbUri = tutor_list.get(position).getImage_thumb();
        //holder.setBlogImage(image_url, thumbUri);

        String user_id = tutor_list.get(position).getUser_id();

        firebaseFirestore.collection("Tutors").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {

                    //String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.setUserData(userImage);

                }else {

                    //Firebase Exception

                }

            }
        });

        try {

            long millisecond = tutor_list.get(position).getTimestamp().getTime();
            //String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(millisecond)).toString();
            //holder.setTime(dateString);

        } catch (Exception e){

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {

        return tutor_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private ImageView tutorImageView;

        private CircleImageView tutorUserImage;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setTutorImage(String downloadUri, String thumbUri){

            //change blog_image to tutor_image
            tutorImageView = mView.findViewById(R.id.blog_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.web_hi_res_512);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(tutorImageView);
        }

        public void setUserData(String image){

            //change blog_user_image to tutor_user_image
            tutorUserImage = mView.findViewById(R.id.blog_user_image);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.addnewpost3);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(tutorUserImage);

        }

    }

}
