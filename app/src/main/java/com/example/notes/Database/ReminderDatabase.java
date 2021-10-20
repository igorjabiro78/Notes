package com.example.notes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notes.ReminderClass;

import java.util.ArrayList;

public class ReminderDatabase extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME = "reminder.db";
    //database version
    public static final int DATABASE_VERSION = 1;
    public static final String table_name = "reminder_table";


    public ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        //creating table
        query = "CREATE TABLE " + table_name + "(ID INTEGER PRIMARY KEY, Event TEXT, date Text , time TEXT)";
        db.execSQL(query);
    }

    //upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    //add the new note
    public void addReminder(String Event, String date, String time) {
        SQLiteDatabase sqLiteDatabase = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Event", Event);
        values.put("date", date);
        values.put("time", time);

        //inserting new row
        sqLiteDatabase.insert(table_name, null , values);
        //close database connection
        sqLiteDatabase.close();
    }
    // add insert all once it fails to add... parameter as reminderClass

    //get the all notes
    public ArrayList<ReminderClass> getReminders() {
        ArrayList<ReminderClass> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + table_name;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ReminderClass reminders = new ReminderClass();
                reminders.setId(Integer.parseInt(cursor.getString(0))); // guhindura string into int
                reminders.setEvent(cursor.getString(1));
                reminders.setDate(cursor.getString(1));
                reminders.setTime(cursor.getString(2));
                arrayList.add(reminders);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    //delete the note
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(table_name, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

    //update the note
    public void updateReminder(String event, String date,String time, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("Event", event);
        values.put("date", date);
        values.put("time", time);
        //updating row
        sqLiteDatabase.update(table_name, values, "ID=" + ID, null);
        sqLiteDatabase.close();
    }
}
