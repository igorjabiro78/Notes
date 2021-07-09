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
import java.util.List;

import javax.sql.DataSource;

public class databaseHelper extends SQLiteOpenHelper {

    public static final String notes_table = "notes_table";
    public static final String titles = "titles";
    public static final String mynotes = "text";
    public static final String noteid = "id";
    public static final String Database_name = "notes.db";
    public static final String Drop_Table = "drop table if exists "+notes_table;
    private Context context;

    public databaseHelper( Context context) {

        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String createTableStatement= "CREATE TABLE "+notes_table+"("+noteid + " INTEGER PRIMARY KEY AUTOINCREMENT, "+titles+"TEXT,"+
                                                                 mynotes+" TEXT)";
       db.execSQL(createTableStatement);
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

    public  boolean addNotes(Notes notes){
     SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(mynotes, notes.getText());
        cv.put(titles, notes.getTitles());

        long insert = db.insert(notes_table, null, cv);
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }

    }

//
//
//
//    public boolean DeleteNotes(Notes notes){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String queryString = "DELETE FROM "+ notes_table +" WHERE "+noteid+" = "+Notes.getId();
//        Cursor cursor = db.rawQuery(queryString, null);
//        if(cursor.moveToFirst()){
//            return true;
//        }
//        else{
//            return false;
//        }
//
//    }
//
    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete(notes_table, "ID=" + ID, null);
        sqLiteDatabase.close();
    }

//
//
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

                Notes notes = new Notes(id,titles,texts);
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
//
    public void updateCourse(Notes notes) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //  passing all value along with its key and value pair.
        cv.put(titles, notes.getTitles());
        cv.put(mynotes, notes.getText());

        //  calling a update method to update our database and passing our values.

        db.update(notes_table, cv,noteid+ "	= ?", new String[] { String.valueOf(Notes.getId())});
        db.close();
    }
//
//



}
