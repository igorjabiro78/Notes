package com.example.notes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class Deleted_Notes extends Fragment {
    private RecyclerView deleteRecycle;
    private RecyclerView.LayoutManager layoutManager;
    public Context ctx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_deleted__notes, container, false);

      ctx = view.getContext();

        deleteRecycle = view.findViewById(R.id.deleteNoteRecycle);
        layoutManager = new LinearLayoutManager(view.getContext());
        deleteRecycle.setLayoutManager(layoutManager);

        fetch_info(add_notes.getuserid(ctx));



       return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        fetch_info(add_notes.getuserid(ctx));
    }
    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
        fetch_info(add_notes.getuserid(ctx));
    }

    void fetch_info(String id) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://192.168.43.242/www/Notes%20Project/access_Method/note_access_method.php?category=getDeletedNotes&useid="+id;
        Log.d("Req", url);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        //convert string into JSONArray format
                        try {

                            JSONArray array = new JSONArray(response); // convert STRING INTO jSON ARRAY
                            if (array.length() > 0) {
                                // String[] listData = fetchnames(array);//convert Json Array into string array

//
                                deleteAdapter adapterIncome = new deleteAdapter(ctx, array);
                                Toast.makeText(ctx,"hello",Toast.LENGTH_LONG).show();
                                deleteRecycle.setAdapter(adapterIncome);
                            }
                        } catch (JSONException ex) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }



}
