package com.example.samsung.UniTeach;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class BookRecyclerAdapter extends  RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    public List<BookPost> book_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    //private PopupWindow popWindow;

    public BookRecyclerAdapter(List<BookPost> book_list) {

        this.book_list = book_list;
    }

    @Override
    public BookRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new BookRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String bookPostId = book_list.get(position).BookPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = book_list.get(position).getDesc();
        //holder.setDescText(desc_data);

        String image_url = book_list.get(position).getImage_url();
        String thumbUri = book_list.get(position).getImage_thumb();
        holder.setBookImage(image_url, thumbUri);

        String user_id = book_list.get(position).getUser_id();
        //get user id retrieve here
        firebaseFirestore.collection("Books").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.setUserData(userName, userImage);

                } else {

                    //Firebase Exception

                }

            }
        });

        try {

            long millisecond = book_list.get(position).getTimestamp().getTime();
            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(millisecond)).toString();
            holder.setTime(dateString);

        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {

        return book_list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private TextView descView;
        private ImageView bookImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserImage;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;



        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            //blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            //blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);

        }

        /*public void setDescText(String descText){

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);
        }*/

        public void setBookImage(String downloadUri, String thumbUri){

            bookImageView = mView.findViewById(R.id.book_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.web_hi_res_512);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(bookImageView);
        }

        public void setTime(String date){

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }

        public void setUserData(String name, String image){

            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogUserName = mView.findViewById(R.id.blog_user_name);

            blogUserName.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.addnewpost3);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(blogUserImage);
        }
    }
}