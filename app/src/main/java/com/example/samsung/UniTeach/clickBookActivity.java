package com.example.samsung.UniTeach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class clickBookActivity extends AppCompatActivity {

    private Toolbar viewBookToolbar;

    private ImageView viewBook;
    private TextView viewDescription;
    private TextView viewEdition;
    private TextView viewPrice;
    private Button MessageSellerButton;
    private FirebaseFirestore FireBaseDB;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_book);
        FireBaseDB = FirebaseFirestore.getInstance();
        tag = getIntent().getStringExtra("id");


        viewBookToolbar = findViewById(R.id.view_book_toolbar);
        setSupportActionBar(viewBookToolbar);
        getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewBook = findViewById(R.id.view_book);
        viewDescription = findViewById(R.id.view_title);
        viewEdition = findViewById(R.id.view_edition);
        viewPrice = findViewById(R.id.view_price);

        MessageSellerButton = findViewById(R.id.message_seller_btn);

        MessageSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(clickBookActivity.this, MessageBookSeller.class);
                startActivity(messageIntent);
            }
        });

       // FireBaseDB.collection("Books")

        String BookDesc = getIntent().getExtras().getString("title");
        String BookEdition = getIntent().getExtras().getString("edition");
        String BookPrice = getIntent().getExtras().getString("price");
        String ImageURL = getIntent().getExtras().getString("image_url");

        viewDescription.setText(BookDesc);
        viewEdition.setText(BookEdition);
        viewPrice.setText(BookPrice);
        Uri imgUri=Uri.parse(ImageURL);
        viewBook.setImageURI(imgUri);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == android.R.id.home){
            //ends activity
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}