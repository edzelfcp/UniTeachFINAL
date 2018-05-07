package com.example.samsung.UniTeach;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class clickTutorActivity extends AppCompatActivity {

    private Toolbar viewTutorToolbar;

    private CircleImageView viewTutor;
    private TextView viewName;
    private TextView viewMajor;
    private TextView viewUni;
    private TextView viewEmail;

    private TextView viewSubjects;

    private TextView viewVerified;

    private Button MessageTutorButton;
    private FirebaseFirestore FireBaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_tutor);
        FireBaseDB = FirebaseFirestore.getInstance();

        viewTutorToolbar = findViewById(R.id.view_tutor_toolbar);
        setSupportActionBar(viewTutorToolbar);
        getSupportActionBar().setTitle("Tutor Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewTutor = findViewById(R.id.profileimage);
        viewName = findViewById(R.id.tutorName);
        viewMajor = findViewById(R.id.tutorMajor);
        viewUni = findViewById(R.id.tutorUni);
        viewEmail = findViewById(R.id.tutorEmail);

        viewVerified = findViewById(R.id.verifiedTutor);

        viewSubjects = findViewById(R.id.tutorSubjects);

        MessageTutorButton = findViewById(R.id.sendMsgTutor);

        MessageTutorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageIntent = new Intent(clickTutorActivity.this, MessageTutor.class);
                startActivity(messageIntent);
            }
        });


        String tutorName = getIntent().getExtras().getString("tutorName");
        String ImageURL = getIntent().getExtras().getString("image_url");
        String tutorMajor = getIntent().getExtras().getString("tutorMajor");
        String tutorUni = getIntent().getExtras().getString("tutorUni");
        String tutorEmail = getIntent().getExtras().getString("tutorMail");

        String tutorVerified = getIntent().getExtras().getString("tutorVerified");

        String tutorSubject = getIntent().getExtras().getString("tutorSubjects");

        viewName.setText(tutorName);
        viewMajor.setText(tutorMajor);
        viewUni.setText(tutorUni);
        viewEmail.setText(tutorEmail);
        viewVerified.setText(tutorVerified);

        viewSubjects.setText(tutorSubject);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            try {
                Uri uri = Uri.parse(ImageURL);
                URL url = new URL(ImageURL);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                viewTutor.setImageBitmap(bmp);
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
