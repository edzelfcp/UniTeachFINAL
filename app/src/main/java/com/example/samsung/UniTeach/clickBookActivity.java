package com.example.samsung.UniTeach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;

public class clickBookActivity extends AppCompatActivity {

    private Toolbar viewBookToolbar;

    private ImageView viewBook;
    private TextView viewDescription;
    private TextView viewEdition;
    private TextView viewPrice;
    private TextView viewEmail;
    private Button MessageSellerButton;
    private FirebaseFirestore FireBaseDB;
    private String tag;
    public Drawable draw;

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
        viewEmail = findViewById(R.id.seller_email);

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
        String Emailview = getIntent().getExtras().getString("email");

        viewDescription.setText(BookDesc);
        viewEdition.setText(BookEdition);
        viewPrice.setText(BookPrice);
        viewEmail.setText(Emailview);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    Uri uri = Uri.parse(ImageURL);
                    URL url = new URL(ImageURL);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    viewBook.setImageBitmap(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("test", "executed image drawable");

        }
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