package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.notes.Database.databaseHelper;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.notes.Database.databaseHelper.sync_status_failed;
import static com.example.notes.Database.databaseHelper.sync_status_ok;

public class add_notes extends AppCompatActivity {
    public Button save, ocr;
    public CheckBox stay;
    public EditText mynotes, title;
    public Context ctx;

    //added code
    AlertDialog.Builder builder;
    public static String server_url = "http://10.0.2.2/Connotes.php";
    private RequestQueue requestQueue;
    private String tag_json_obj="json_obj_req";
    private String tag_success="success",tag_message="message";
    private int success;

    databaseHelper databasehelper;
    private static final int RC_OCR_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        databasehelper = new databaseHelper(this);

        // builder dialog
        builder = new AlertDialog.Builder(add_notes.this);

        save = findViewById(R.id.save);
        mynotes = findViewById(R.id.editnotes);
        title = findViewById(R.id.edittitle);
        ocr = findViewById(R.id.ocr);
        stay = findViewById(R.id.stay);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        ctx = getApplicationContext();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    if(mynotes.getText().toString().equals("") || title.getText().toString().equals(""))
                        Toast.makeText(ctx,"empty",Toast.LENGTH_SHORT).show();
                    else
                        saveToAppServer(title.getText().toString(),mynotes.getText().toString());
                        Toast.makeText(add_notes.this,"saved",Toast.LENGTH_LONG).show();


                    //to do : check for internet connection  first
                    sendData();

                }
                catch (Exception e){
                    Toast.makeText(add_notes.this,"Error in adding"+e.getMessage(),Toast.LENGTH_LONG).show();

                }

                if(stay.isChecked()){
                    title.setText("");
                    mynotes.setText("");
                } else{
                    finish();
                    startActivity(new Intent(ctx,Notes_Tabs.class));
                }

            // added code to connect to db
            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            builder.setTitle("Server Response");
                            builder.setMessage("response: "+response);
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    title.setText("");
                                    mynotes.setText("");
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(add_notes.this,"Error..",Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("title",title.getText().toString());
                    params.put("text",mynotes.getText().toString());

                    return params;
                }
            };

              MySingleton.getInstance(add_notes.this).addToRequestque(stringRequest);




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

//    public void addmyTexts(View view){
//        String titles = title.getText().toString();
//        String text =mynotes.getText().toString();
//        Notes notes = new Notes(titles,text,sync_status_failed);
//        databasehelper.saveToLocalDatabase(titles,text,sy);
////
//    }



    public void saveToAppServer(String title,String name){

        databasehelper = new databaseHelper(ctx);
        SQLiteDatabase database = databasehelper.getWritableDatabase();



        if(checkNetworkConnections(ctx)){

            sendData();
        }

        else{
            saveToLocalStorages(title,name,sync_status_failed);

        }
        MainActivity mainActivity = new MainActivity();
        mainActivity.readFromLocalStorage();
       databasehelper.close();

    }

    public void saveToLocalStorages(String title,String name,int sync){

        databasehelper = new databaseHelper(ctx);
        SQLiteDatabase database = databasehelper.getWritableDatabase();
        databasehelper.saveToLocalDatabase(title,name,sync);

        MainActivity mainActivity = new MainActivity();
        mainActivity.readFromLocalStorage();
        databasehelper.close();


    }



    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            Log.d("application","Network available");
            return true;
        }else{
            Log.d("application","Networrk not available");
            return false;
        }


    }

    public boolean checkNetworkConnections(Context context)
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo  =connectivityManager.getActiveNetworkInfo();
        return(networkInfo!=null && networkInfo.isConnected());
//        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
//        {
//            Log.d("application","Network available");
//            return true;
//        }else{
//            Log.d("application","Networrk not available");
//            return false;
    }





    private void sendData(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject= new JSONObject(response);
                            String Response =jsonObject.getString("response");
//                            success = jsonObject.getInt(tag_success);
                            if(Response.equals("OK")){
                               saveToLocalStorages(title.getText().toString(),mynotes.getText().toString(),sync_status_ok);
                            }
                            else{
                                saveToLocalStorages(title.getText().toString(),mynotes.getText().toString(),sync_status_failed);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                saveToLocalStorages(title.getText().toString(),mynotes.getText().toString(),sync_status_failed);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title",title.getText().toString());
                params.put("text",mynotes.getText().toString());

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000,1,1.0f));
        requestQueue.add(stringRequest);

        MySingleton.getInstance(add_notes.this).addToRequestque(stringRequest);
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