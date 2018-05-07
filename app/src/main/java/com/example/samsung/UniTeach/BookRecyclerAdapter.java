package com.example.samsung.UniTeach;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    public List<BookPost> book_list;
    public List<User> user_list;
    public Context context;
    public String desc_data;
    public String edition_desc;
    public String price_desc;
    public String email_desc;
    public String image_url;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BookRecyclerAdapter(List<BookPost> book_list, List<User> user_list) {
        this.book_list = book_list;
        this.user_list = user_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //BookRecyclerAdapter.ViewHolder onCreateViewHolder

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        context = parent.getContext();


        return new BookRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        int pass_position = position;
        String a = String.valueOf(pass_position);
        System.out.println(a);

        holder.setIsRecyclable(false);
        final String bookPostId = book_list.get(position).BookPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        //this is for getter and setter
        desc_data = book_list.get(pass_position).getDescription();
        holder.setDescription(desc_data);

        edition_desc= book_list.get(pass_position).getEdition();
        holder.setEdition(edition_desc);

        price_desc= book_list.get(pass_position).getPrice();
        holder.setPrice(price_desc);

        image_url = book_list.get(pass_position).getImage_url();
        String thumbUri = book_list.get(pass_position).getImage_thumb();
        holder.setBookImage(image_url, thumbUri);

        email_desc = book_list.get(pass_position).getEmail();
        holder.setEmail(email_desc);

        //delete button
       String book_user_id = book_list.get(position).getUser_id();
       if(book_user_id.equals(currentUserId)){
           holder.bookdeletebtn.setEnabled(true);
           holder.bookdeletebtn.setVisibility(View.VISIBLE);
       }

        holder.seeDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent clickPost = new Intent(context, clickBookActivity.class);
                //clickPost.putExtra("id",desc_data);

                clickPost.putExtra("title", desc_data);
                clickPost.putExtra("edition", edition_desc);
                clickPost.putExtra("price", price_desc);
                clickPost.putExtra("image_url", image_url);
                clickPost.putExtra("email", email_desc);

                context.startActivity(clickPost);
            }
        });



       holder.bookdeletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Books").document(bookPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        book_list.remove(position);
                        user_list.remove(position);
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {

        return book_list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;

        private ImageView bookImageView;

        private Button bookdeletebtn;
        private TextView bdescView;
        private TextView editionView;
        private TextView priceView;

        private TextView emailView;

        private Button seeDetails;


        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            bookdeletebtn = mView.findViewById(R.id.book_delete_btn);
            seeDetails = mView.findViewById(R.id.see_details_btn);
        }

        public void setEmail(String email_desc) {
            emailView = mView.findViewById(R.id.book_email);
            emailView.setText(email_desc);
        }

        public void setDescription(String descText){

            bdescView = mView.findViewById(R.id.book_desc);
            bdescView.setText(descText);

        }

        public void setEdition(String edText) {

            editionView = mView.findViewById(R.id.view_edition);
            editionView.setText(edText);
        }

        public void setPrice(String pText) {

            priceView = mView.findViewById(R.id.book_price);
            priceView.setText(pText);
        }


        public void setBookImage(String downloadUri, String thumbUri){

            bookImageView = mView.findViewById(R.id.book_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.addnewpost3);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(bookImageView);
        }



    }
}