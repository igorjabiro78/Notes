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
import com.example.notes.Database.databaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Deleted_Notes extends Fragment {
    private RecyclerView deleteRecycle;
    private RecyclerView.LayoutManager layoutManager;
    com.example.notes.Database.databaseHelper databaseHelper;
    public Context ctx;
    ArrayList<Notes> mynoteList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_deleted__notes, container, false);

      ctx = view.getContext();

        deleteRecycle = view.findViewById(R.id.deleteNoteRecycle);
        layoutManager = new LinearLayoutManager(view.getContext());
        deleteRecycle.setLayoutManager(layoutManager);

//        fetch_info(add_notes.getuserid(ctx));


        databaseHelper = new databaseHelper(ctx);
       deleteAdapter deleteAdapter = new deleteAdapter(mynoteList);
       deleteRecycle.setAdapter(deleteAdapter);


       return view;

    }
    @Override
    public void onResume() {
        super.onResume();
//        fetch_info(add_notes.getuserid(ctx));
    }
    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
//        fetch_info(add_notes.getuserid(ctx));
    }



    }




