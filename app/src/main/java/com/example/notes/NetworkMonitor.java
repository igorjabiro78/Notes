package com.example.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.notes.Database.databaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.notes.Database.databaseHelper.SYNC_STATUS;
import static com.example.notes.add_notes.server_url;

public class NetworkMonitor extends BroadcastReceiver {
    public static final String UI_UPDATE_BROADCAST = "com.example.notes.uiupdatebroadcast";
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(checkNetworkConnections(context))
        {
            final databaseHelper databaseHelper = new databaseHelper(context);
            SQLiteDatabase database = databaseHelper.getWritableDatabase();

            Cursor cursor = databaseHelper.readfromLocalDatabase(database);

            while(cursor.moveToNext()){
                final int sync_status = cursor.getInt(cursor.getColumnIndex(SYNC_STATUS));
                if(sync_status == com.example.notes.Database.databaseHelper.sync_status_failed)
                {
                   final String title = cursor.getString(cursor.getColumnIndex(com.example.notes.Database.databaseHelper.titles));
                  final  String notes = cursor.getString(cursor.getColumnIndex(com.example.notes.Database.databaseHelper.mynotes));
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String Response = jsonObject.getString("response");
                                        if(Response.equals("OK")){
                                            databaseHelper.updateLocalDatabase(title,notes,sync_status);
                                            context.sendBroadcast(new Intent(UI_UPDATE_BROADCAST));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("title",title);
                            params.put("text",notes);

                            return params;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestque(stringRequest);
                }

            }
            databaseHelper.close();
        }



    }
    public boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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


    }


