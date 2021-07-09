package com.example.notes;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.Database.DataSource;
import com.example.notes.Database.databaseHelper;
import com.example.notes.Database_for_notification.DatabaseClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment {

    DatabaseClass databaseClass;
//    EventAdapter eventAdapter;

    public FloatingActionButton fabnote;
    private RecyclerView noteRecycle;
    private RecyclerView.LayoutManager layoutManager;
    com.example.notes.Database.databaseHelper databaseHelper;
    notesAdapter notesAdapter;
    List<Notes> mynoteList = new ArrayList<Notes>();

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
        reminder = view.findViewById(R.id.reminder);

        databaseClass = DatabaseClass.getDatabase(ctx);

        databaseHelper = new databaseHelper(ctx);

        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

//        Toast.makeText(ctx,"here we go",Toast.LENGTH_LONG).show();
        mynoteList = databaseHelper.getAll();
       notesAdapter = new notesAdapter(ctx,mynoteList,noteRecycle);
       noteRecycle.setAdapter(notesAdapter);





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
                 startActivity(new Intent(ctx,date_picker.class));
             }
         });



//      fetch_Data(add_notes.getuserid(ctx));
        return view;


    }
    private void displayList(List<Notes> allNotes) {
        noteRecycle.setLayoutManager(new LinearLayoutManager(ctx));
        notesAdapter = new notesAdapter(ctx,allNotes,noteRecycle);
        noteRecycle.setAdapter(notesAdapter);
    }

//    private void setAdapter() {
//        List<EntityClass> classList = databaseClass.EventDao().getAllData();
//        eventAdapter = new EventAdapter(ctx.getApplicationContext(), classList);
//        recyclerview.setAdapter(eventAdapter);
//    }

    /*aho bigeze,
      - check set adapter class and entity class
      - test project
      - also view other notes app that used sqlitez
    * */

    @Override
    public void onResume() {
        super.onResume();
        displayList(mynoteList);
//        fetch_Data(add_notes.getuserid(ctx));
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



}
