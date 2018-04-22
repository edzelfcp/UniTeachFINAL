package com.example.samsung.UniTeach;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder> {

    public List<BookPost> book_list;
    public Context context;

    public BookRecyclerAdapter(List<BookPost> book_list) {
        this.book_list = book_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //BookRecyclerAdapter.ViewHolder onCreateViewHolder

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        context = parent.getContext();

        return new BookRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        //this is for getter and setter
        String desc_data = book_list.get(position).getDescription();
        holder.setDescription(desc_data);

        String edition_desc= book_list.get(position).getEdition();
        holder.setEdition(edition_desc);

        String price_desc= book_list.get(position).getPrice();
        holder.setPrice(price_desc);

        String image_url = book_list.get(position).getImage_url();
        String thumbUri = book_list.get(position).getImage_thumb();
        holder.setBookImage(image_url, thumbUri);

        holder.bookImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent clickPost = new Intent(context, clickBookActivity.class);
                context.startActivity(clickPost);
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

        private TextView bdescView;
        private TextView editionView;
        private TextView priceView;


        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;
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