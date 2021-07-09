package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class Profile_Data extends AppCompatActivity {
    public Intent intent_signup;
    public TextView myname,gender;

    public String amazina,mypassword,mygender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__data);

        myname = findViewById(R.id.nameText);

        gender= findViewById(R.id.mygender);

        SharedPreferences pref = getSharedPreferences("credential",MODE_PRIVATE);
        String userName = pref.getString("username","");
        String userGender = pref.getString("usergender","");


        myname.setText(userName);
//        password.setText(mypassword);
        gender.setText(userGender);



    }
}
