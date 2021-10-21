package com.example.notes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.notes.Database.ReminderDatabase;
import com.example.notes.Database.databaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.notes.Database.databaseHelper.sync_status_failed;

public class MainActivity extends Fragment {

    ReminderDatabase databaseClass;
//    EventAdapter eventAdapter;

    public FloatingActionButton fabnote;
    private RecyclerView noteRecycle;
    private RecyclerView.LayoutManager layoutManager;
    com.example.notes.Database.databaseHelper databaseHelper;
    notesAdapter notesAdapter;
    ArrayList<Notes> mynoteList = new ArrayList<>();

    BroadcastReceiver broadcastReceiver;

    public FloatingActionButton reminder;

    public Context ctx;
    @Override
    public   View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_main, container, false);

        ctx= view.getContext();

        fabnote = view.findViewById(R.id.addnote);
        //for recycle view
        noteRecycle = view.findViewById(R.id.noteRecycle);
        layoutManager = new LinearLayoutManager(view.getContext());
        noteRecycle.setLayoutManager(layoutManager);
        noteRecycle.setHasFixedSize(true);
        reminder = view.findViewById(R.id.reminder);


        checkNetworkConnections(ctx);

        checkNetworkConnection(ctx);
//        databaseClass = ReminderDatabase.getDatabase(ctx);

        databaseHelper = new databaseHelper(ctx);

//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();


//        mynoteList = databaseHelper.getAll();
//        notesAdapter = new notesAdapter(ctx,mynoteList,noteRecycle);// comment this out
        notesAdapter = new notesAdapter(mynoteList);
        noteRecycle.setAdapter(notesAdapter);

        readFromLocalStorage();

        broadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               readFromLocalStorage();
            }
        };

        fabnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(view.getContext(),add_notes.class));
            }
        });

        // lets make edit(update) and delete when we long press

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderFragment reminder = new ReminderFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.rlayoutreminder,reminder);
                transaction.commit();
            }
        });

        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        ctx.registerReceiver(broadcastReceiver,new IntentFilter(NetworkMonitor.UI_UPDATE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        ctx.unregisterReceiver(broadcastReceiver);
    }

    private void displayList(ArrayList<Notes> allNotes) {
        noteRecycle.setLayoutManager(new LinearLayoutManager(ctx));
        notesAdapter = new notesAdapter(ctx,allNotes,noteRecycle);
        noteRecycle.setAdapter(notesAdapter);
    }



    @Override
    public void onResume() {
        super.onResume();
        displayList(mynoteList);
//        fetch_Data(add_notes.getuserid(ctx));
    }

     public  void readFromLocalStorage()
     {
         mynoteList.clear();
         databaseHelper = new databaseHelper(ctx);
         SQLiteDatabase database = databaseHelper.getReadableDatabase();

         Cursor cursor = databaseHelper.readfromLocalDatabase(database);
         while(cursor.moveToNext())
         {
             String title = cursor.getString(cursor.getColumnIndex(com.example.notes.Database.databaseHelper.titles));
             String mynotes = cursor.getString(cursor.getColumnIndex(com.example.notes.Database.databaseHelper.mynotes));
             int sync = cursor.getInt(cursor.getColumnIndex(com.example.notes.Database.databaseHelper.SYNC_STATUS));
             Notes notes = new Notes(title,mynotes,sync);

             mynoteList.add(notes);

         }
         notesAdapter.notifyDataSetChanged();
         cursor.close();
         databaseHelper.close();
     }


    public boolean checkNetworkConnections(Context context)
    {
        ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo  =connectivityManager.getActiveNetworkInfo();
        return(networkInfo!=null && networkInfo.isConnected());
//
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



    // fetch info

//    public void displayNotes() {
//        ArrayList arrayList = new ArrayList<>(databaseHelper.getAll());
//        JSONArray jsArray = new JSONArray(arrayList);
//        noteRecycle.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));
//        noteRecycle.setItemAnimator(new DefaultItemAnimator());
//        notesAdapter adapter = new notesAdapter(ctx,  jsArray);
//        noteRecycle.setAdapter(adapter);
//    }


//    void fetch_Data(String id) {
//        RequestQueue queue = Volley.newRequestQueue(ctx);
//        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=getData&useid="+id;
//        Log.d("Req", url);
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
//                        try {
//                            JSONArray array = new JSONArray(response); // copnvert string to json array
//                            Log.d("Resp",array.toString());
//                            if (array.length() > 0) {
////                                String[] listExpenses = convertData(array); // convert from json to string array
//                                //hold data to print
//
//                                notesAdapter adaptExpenses = new notesAdapter(ctx, array);
//
//                                noteRecycle.setAdapter(adaptExpenses);
//
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ctx, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//
//    }


//    public boolean checkNetworkConnection(){
//        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(getContext())
//    }


}
