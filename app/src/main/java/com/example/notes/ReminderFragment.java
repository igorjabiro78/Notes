package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.notes.Database.ReminderDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReminderFragment extends Fragment {

    private RecyclerView reminderRecycle;
    private ListView reminderList;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fabreminder;
    public Context ctx;
    ReminderDatabase reminderDatabase;
    ArrayList<ReminderClass> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_reminders, container, false);

        ctx = view.getContext();

        reminderRecycle = (RecyclerView) view.findViewById(R.id.reminderRecycleView);
//        reminderList = (ListView) view.findViewById(R.id.reminderList);
        layoutManager = new LinearLayoutManager(ctx);
        reminderRecycle.setLayoutManager(layoutManager);

//        reminderRecycle.setHasFixedSize(true);
        reminderRecycle.setLayoutManager(layoutManager);
//        reminderList.setLayoutManager(new LinearLayoutManager(ctx));



        fabreminder = view.findViewById(R.id.fabReminder);
        fabreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ctx,date_picker.class));
            }
        });
        displayReminders();

        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        displayReminders();

    }
    @Override
    public void onAttach(Context ctx){
        super.onAttach(ctx);
        this.ctx = ctx;
        Log.d("dore","here we go");
        displayReminders();

    }
    public void displayReminders() {

        reminderDatabase = new ReminderDatabase(ctx);
        arrayList = reminderDatabase.getReminders();

//        final reminderAdapter remind = new reminderAdapter(ctx, this,arrayList);
//        reminderList.setAdapter((ListAdapter) remind);



//        reminderRecycle.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));
//        reminderRecycle.setLayoutManager(layoutManager);
        reminderRecycle.setItemAnimator(new DefaultItemAnimator());
        reminderAdapter adapter = new reminderAdapter(ctx, this, arrayList);
        reminderRecycle.setAdapter(adapter);
    }
}