package com.example.samsung.UniTeach;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorRecyclerAdapter extends RecyclerView.Adapter<TutorRecyclerAdapter.ViewHolder> {

    public List<TutorPost> tutor_list;
    public Context context;


    public TutorRecyclerAdapter(List<TutorPost> tutor_list) {
        this.tutor_list = tutor_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_list_item, parent, false);
        context = parent.getContext();

        return new TutorRecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String tutName = tutor_list.get(position).getTutorName();
        holder.setTutorName(tutName);

        String image_url = tutor_list.get(position).getImage_url();
        String thumbUri = tutor_list.get(position).getImage_thumb();
        holder.setTutorImage(image_url, thumbUri);

        //String user_id = tutor_list.get(position).getUser_id();

        holder.tutorImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent clickTutor = new Intent(context, clickTutorActivity.class);
                context.startActivity(clickTutor);

            }
        });
    }

    @Override
    public int getItemCount() {

        return tutor_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        //private Button seeTutor;

        private CircleImageView tutorImageView;
        private TextView tutorUserName;

        //private CircleImageView tutorUserImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //idk what for
            //Button seeTutor = mView.findViewById(R.id.tutorViewBtn);
        }

        public void setTutorImage(String downloadUri, String thumbUri){

            tutorImageView = mView.findViewById(R.id.tutorImage);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.web_hi_res_512);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(tutorImageView);
        }

        public void setTutorName(String tutorName) {

            tutorUserName = mView.findViewById(R.id.tutorName);
            tutorUserName.setText(tutorName);
        }

        /*public void setUserData(String name,String image){

            //change blog_user_image to tutor_user_image
            tutorUserImage = mView.findViewById(R.id.tutorImage);
            tutorUserName = mView.findViewById(R.id.tutorName);

            tutorUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.addnewpost3);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(tutorUserImage);

        }*/

    }

}
