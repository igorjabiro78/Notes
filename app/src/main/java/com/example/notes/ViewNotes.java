package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewNotes extends AppCompatActivity {

    public TextView Tvtext, Tvtitle, Tvdate, Tvstat, tvId;
    public LinearLayout lnyNotes;
    public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);


            tvId = findViewById(R.id.itemId);
            Tvtext = findViewById(R.id.Text);
            Tvtitle = findViewById(R.id.Title);
            Tvdate = findViewById(R.id.date);
            Tvstat = findViewById(R.id.status);
            lnyNotes = findViewById(R.id.lnyNotes);

            //reaad intent data
            intent = getIntent();
            tvId.setText(intent.getStringExtra("id"));
            Tvtext.setText(intent.getStringExtra("text"));
            Tvdate.setText(intent.getStringExtra("date"));
            Tvtitle.setText(intent.getStringExtra("title"));

    }
}
