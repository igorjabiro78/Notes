package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notes.Database.DataSource;
import com.example.notes.Database.databaseHelper;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class add_notes extends AppCompatActivity {
    public Button save, ocr;
    public CheckBox stay;
    public EditText mynotes, title;
    public Context ctx;

     databaseHelper databasehelper;
    private static final int RC_OCR_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        save = findViewById(R.id.save);
        mynotes = findViewById(R.id.editnotes);
        title = findViewById(R.id.edittitle);
        ocr = findViewById(R.id.ocr);
        stay = findViewById(R.id.stay);

        ctx = getApplicationContext();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Notes notes = null;
                try {
                    Toast.makeText(ctx,"aha nahageze ",Toast.LENGTH_LONG).show();
                    notes = new Notes(-1,mynotes.getText().toString(), title.getText().toString());
                    if(mynotes.getText().toString().equals("") & title.getText().toString().equals(""))
                        Toast.makeText(ctx,"empty",Toast.LENGTH_SHORT).show();
                    else
                       Toast.makeText(add_notes.this,"saved",Toast.LENGTH_LONG).show();
                    databasehelper = new databaseHelper(add_notes.this);
                    databasehelper.addNotes(notes);
                }
                catch (Exception e){
                    Toast.makeText(add_notes.this,"Error in adding"+e.getMessage(),Toast.LENGTH_LONG).show();

                }
               // databasehelper = new databaseHelper(add_notes.this);

               //d databasehelper.addNotes(notes);


//                saveNotes(notes.getText().toString(), title.getText().toString(),getuserid(ctx));

                if(stay.isChecked()){
                title.setText("");
                notes.setText("");
            } else{
                    finish();
                    startActivity(new Intent(ctx,Notes_Tabs.class));
                }
            }
        });
        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_notes.this, OcrCaptureActivity.class);
                intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                intent.putExtra(OcrCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_OCR_CAPTURE);
            }
        });

    }

    public void addmyTexts(View view){
        String titles = title.getText().toString();
        String text =mynotes.getText().toString();
        Notes notes = new Notes(1,titles,text);
        boolean id = databasehelper.addNotes(notes);
        if(id){
            Toast.makeText(ctx,"successfull",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ctx,"successfull",Toast.LENGTH_SHORT).show();
        }
    }


    void saveNotes(String text, String title, String id) {



        RequestQueue queue = Volley.newRequestQueue(add_notes.this);
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=insertNotes&note= " + text + "&title=" + title+"&useid="+id;
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
    public static void TemporalydeleteNote(final Context context, String id) {

        RequestQueue queue = Volley.newRequestQueue(context); // context kuko twa creatinz static method and we dont need to create an object before accessing
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=TemporalydeleteNotes&id= " + id ;
        Log.d("Req", url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public static void permanentlydeleteNote(final Context context, String id) {

        RequestQueue queue = Volley.newRequestQueue(context); // context kuko twa creatinz static method and we dont need to create an object before accessing
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=deleteNotes&id= " + id ;
        Log.d("Req", url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public static void recoverdeleteNote(final Context context, String id) {

        RequestQueue queue = Volley.newRequestQueue(context); // context kuko twa creatinz static method and we dont need to create an object before accessing
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=recovernotes&id= " + id ;
        Log.d("Req", url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public static void Login(final Context context, String n,String p) {

        RequestQueue queue = Volley.newRequestQueue(context); // context kuko twa creatinz static method and we dont need to create an object before accessing
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/user_access_method.php?category=login&n= " + n + "&p="+p;
        Log.d("Req", url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                        try {
                            // aha nih nshyira preference code
                            JSONArray array = new JSONArray(response); // convert string to JSON array . tuyikuye server(Localhost)
                            if (array.length() > 0) {
                                JSONObject object = array.getJSONObject(0);
                                SharedPreferences sharedpreferences = context.getSharedPreferences("credential", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("userid", object.getString("id")); // name:id abari column in database
                                editor.putString("username", object.getString("name")); // username,usergender are variables. they are the ones you pass when reading
                                editor.putString("usergender", object.getString("gender"));
                                editor.commit(); // or you can use editor.apply()

                                Intent intent = new Intent(context,Notes_Tabs.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            else{
                                Toast.makeText(context,"Wrong Name or Password "+response,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException ex) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    public static String getuserid(Context context){

        SharedPreferences pref = context.getSharedPreferences("credential",MODE_PRIVATE);
        String id = pref.getString("userid","");

        return id;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent obj) {
        super.onActivityResult(requestCode, resultCode, obj);
        if (obj != null) {
            if (requestCode == RC_OCR_CAPTURE) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    Intent intent = obj;
                    String text = intent.getStringExtra("String");
                    mynotes.setText(text);
                    // nigute text ya Title nayo twayiscanninga

                }

            }
        }
    }
}