package com.example.samsung.UniTeach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class clickTutorActivity extends AppCompatActivity {

    private Toolbar viewTutorToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_tutor);

        viewTutorToolbar = findViewById(R.id.view_tutor_toolbar);
        setSupportActionBar(viewTutorToolbar);
        getSupportActionBar().setTitle("Tutor Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
