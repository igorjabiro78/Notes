package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.notes.Database.ReminderDatabase;
import com.example.notes.Database.databaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.notes.Database.databaseHelper.sync_status_failed;

public class MainActivity extends Fragment {

    ReminderDatabase databaseClass;
//    EventAdapter eventAdapter;

    ActionBarDrawerToggle toggle;
    public DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    public NavigationView navigationView;


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

        reminder = view.findViewById(R.id.reminder);


        checkNetworkConnections(ctx);

//        checkNetworkConnection(ctx);
//        databaseClass = ReminderDatabase.getDatabase(ctx);

        databaseHelper = new databaseHelper(ctx);

//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

//        notesAdapter = new notesAdapter(mynoteList);
//        noteRecycle.setAdapter(notesAdapter);
        displayList(mynoteList);

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


        //nav drawer
//        Toolbar toolbar=view.findViewById(R.id.toolbar);
//
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        drawerLayout=view.findViewById(R.id.drawer);
//        tabLayout =  view.findViewById(R.id.tabs);
//        navigationView=view.findViewById(R.id.nav_view);
//        toggle=new ActionBarDrawerToggle((Activity) ctx,drawerLayout,toolbar,R.string.open,R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.setDrawerIndicatorEnabled(true);
//        toggle.syncState();
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
//            {
//                switch (menuItem.getItemId())
//                {
//                    case R.id.home :
//                        Toast.makeText(ctx,"Home Panel is Open",Toast.LENGTH_LONG).show();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
//                    case R.id.settings :
//                        Toast.makeText(ctx,"Setting Panel is Open",Toast.LENGTH_LONG).show();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
//                    case R.id.bin :
//                        Toast.makeText(ctx,"Trash bin is Open",Toast.LENGTH_LONG).show();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//                }
//
//                return true;
//            }
//        });

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

    }

     public void readFromLocalStorage()
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
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
            Toast.makeText(ctx,"internet connection",Toast.LENGTH_SHORT).show();
        }
        return haveConnectedWifi || haveConnectedMobile;

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




}
