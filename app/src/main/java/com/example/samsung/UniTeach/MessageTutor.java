package com.example.samsung.UniTeach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MessageTutor extends AppCompatActivity {

    private Toolbar tutormessage;
    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_tutor);

        tutormessage = findViewById(R.id.message_tutor_toolbar);
        setSupportActionBar(tutormessage);
        //getSupportActionBar().setTitle("Book Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        send = findViewById(R.id.send);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String To = mEditTextTo.getText().toString();
                String textSubject = mEditTextSubject.getText().toString();
                String textMessage = mEditTextMessage.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {To});
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
