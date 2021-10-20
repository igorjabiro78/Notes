package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

public class Sign_Up extends AppCompatActivity {
    public Button sign;
    public EditText name,email, password;
    private TextView bypass;

    public RadioButton radioButton;
    private RadioGroup group;


    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String mygender = "genderKey";

    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        sign = findViewById(R.id.sign);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        bypass = findViewById(R.id.bypass);
        email = findViewById(R.id.emails);




        bypass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_Up.this, Notes_Tabs.class));
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You clicked ", Toast.LENGTH_LONG).show();


//                saveIncome(name.getText().toString(), password.getText().toString(), gender);


            }
        });


    }



}
