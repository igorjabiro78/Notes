package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public Button login;
    public TextView createAccount;
    public EditText izina,password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        izina = findViewById(R.id.loginame);
        password = findViewById(R.id.loginpassword);

        createAccount = findViewById(R.id.create);
        login = findViewById(R.id.login);


      login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              // call add_notes
              if (izina.getText().toString().trim().isEmpty() || password.getText().toString().isEmpty()) {
                  Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
              } else {
                  add_notes.Login(getApplicationContext(), izina.getText().toString().trim(), password.getText().toString().trim());
              }
          }
      });

      createAccount.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(Login.this,Sign_Up.class));
          }
      });

    }
}
