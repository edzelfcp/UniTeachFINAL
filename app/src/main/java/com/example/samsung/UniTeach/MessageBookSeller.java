package com.example.samsung.UniTeach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

public class MessageBookSeller extends AppCompatActivity {

    private Toolbar bookmessage;
    private EditText editTextTo;
    private EditText editTextSubject;
    private EditText editTextMessage;
    private Button messageSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_book_seller);

        bookmessage = findViewById(R.id.Book_Message);
        setSupportActionBar(bookmessage);
        //getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTo = findViewById(R.id.book_message_to);
        editTextSubject = findViewById(R.id.book_subject);
        editTextMessage = findViewById(R.id.book_message_details);
        messageSeller = findViewById(R.id.send_btn);

        messageSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String To = editTextTo.getText().toString();
                String textSubject = editTextSubject.getText().toString();
                String textMessage = editTextMessage.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{To});
                intent.putExtra(Intent.EXTRA_SUBJECT, textSubject);
                intent.putExtra(Intent.EXTRA_TEXT, textMessage);

                startActivity(Intent.createChooser(intent, "Select Action"));
            }
        });

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
