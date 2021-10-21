package com.example.notes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.notes.Notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class databaseHelper extends SQLiteOpenHelper {

    public static final String notes_table = "notes_table";
    public static final int database_version = 1;

    public static final int sync_status_ok = 0;
    public static final int sync_status_failed = 1;

    public static final String mynotes = "text";
    public static final String titles = "title";
    public static final String noteid = "id";
    public static final String SYNC_STATUS = "sync";
//    public static final String date = "date_created";    for dates created

    public static final String Database_name = "notes";
    public static final String Drop_Table = "drop table if exists "+notes_table;
    private Context context;

    public databaseHelper( Context context) {

        super(context, Database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement= "CREATE TABLE " + notes_table + " (id integer primary key autoincrement, " + titles + " TEXT, "+
                mynotes + " TEXT, " + SYNC_STATUS + " TEXT);";

       db.execSQL(createTableStatement);

//        db.execSQL(createTableStatement);
//        db.insert(Database_name,null, DataSource.getDefaultCategory());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            Toast.makeText(context,"on upgrade called",Toast.LENGTH_SHORT).show();
            db.execSQL(Drop_Table);
            onCreate(db);
        }
        catch (SQLException e){
            Toast.makeText(context,""+e,Toast.LENGTH_SHORT).show();
        }
    }


    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(notes_table, "ID=" + ID, null);
        sqLiteDatabase.close();
    }


    public ArrayList<Notes> getAll() {
        ArrayList<Notes> returnList = new ArrayList<>();
        String querry = "SELECT * FROM "+notes_table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(querry,null);
        if(cursor.moveToFirst()){
            do{
                int id= cursor.getInt(0);
                String titles= cursor.getString(1);
                String texts = cursor.getString(2);
                int sync = cursor.getInt(3);

                Notes notes = new Notes(titles,texts,sync);
                returnList.add(notes);
            }
            while (cursor.moveToNext());
        }
        else {
        }
        cursor.close();
        db.close();

        return returnList;
    }

    //
//
   public void saveToLocalDatabase(String title,String mynote,int sync){

       SQLiteDatabase db= this.getWritableDatabase();
       ContentValues cv = new ContentValues();
       cv.put(titles, title);
       cv.put(mynotes, mynote);
       cv.put(SYNC_STATUS, sync);

       db.insert(notes_table, null, cv);
       db.close();


   }

    public void updateLocalDatabase(String title,String name,int sync) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //  passing all value along with its key and value pair.
//        cv.put(titles, title);
//        cv.put(mynotes, name);
        cv.put(SYNC_STATUS, sync);
        String selection = titles+" LIKE ?";
        String [] selection_args = {title,name};

        //  calling a update method to update our database and passing our values.

        db.update(notes_table, cv,selection, selection_args);
        db.close();
    }

    public Cursor readfromLocalDatabase(SQLiteDatabase database)
    {
        String[] projection = {titles,mynotes,SYNC_STATUS};
        return (database.query(notes_table,projection,null,null,null,null,null));
    }



}
