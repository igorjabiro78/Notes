package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.snackbar.Snackbar;

public class Edit_Texts extends AppCompatActivity {

    public Button save, ocr;
    public EditText notes, title;
    public Intent Intent_edit;
    String id,ti,te;

    private static final int RC_OCR_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__texts);



        save = findViewById(R.id.save);
        notes = findViewById(R.id.editnotes);
        title = findViewById(R.id.edittitle);
        ocr = findViewById(R.id.ocr);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatenote(id,notes.getText().toString(), title.getText().toString());
            }
        });
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Edit_Texts.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });


        Intent_edit = getIntent();
        id = Intent_edit.getStringExtra("id");
        ti = Intent_edit.getStringExtra("title");
        te = Intent_edit.getStringExtra("text");

        notes.setText(te);
        title.setText(ti);


    }


    void updatenote(String id,String text, String title) {

        RequestQueue queue = Volley.newRequestQueue(Edit_Texts.this);
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=updateNotes&id= " + id + "&note= " + text + "&title=" + title;
        Log.d("Req", url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Snackbar.make(save, response, Snackbar.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent obj) {
        super.onActivityResult(requestCode, resultCode, obj);
        if (obj != null) {
            if (requestCode == RC_OCR_CAPTURE) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    Intent intent = obj;
                    String text = intent.getStringExtra("String");
                    notes.setText(text);
                    // nigute text ya Title nayo twayiscanninga

                }

            }
        }
    }
}
