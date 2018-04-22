package com.example.samsung.UniTeach;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class clickBookActivity extends AppCompatActivity {

    private Toolbar viewBookToolbar;

    private ImageView viewBook;
    private TextView viewDescription;
    private TextView viewEdition;
    private TextView viewPrice;
    //private Button DeleteBookButton, EditBookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_book);

        viewBookToolbar = findViewById(R.id.view_book_toolbar);
        setSupportActionBar(viewBookToolbar);
        getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewBook = findViewById(R.id.view_book);
        viewDescription = findViewById(R.id.view_title);
        viewEdition = findViewById(R.id.view_edition);
        viewPrice = findViewById(R.id.view_price);

        //String BookDesc = getIntent().getExtras().getString("title");
        //String BookEdition = getIntent().getExtras().getString("edition");
        //String BookPrice = getIntent().getExtras().getString("price");

        //viewDescription.setText(BookDesc);
        //viewEdition.setText(BookEdition);
        //viewPrice.setText(BookPrice);

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