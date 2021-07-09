package com.example.notes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.notes.Notes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * The type Data source.
 *
 * @author Harry Dulaney
 */
public class DataSource {

    private final String TAG = this.getClass().getSimpleName();
    private SQLiteDatabase database;
    private databaseHelper db;



    /**
     * Instantiates a new Data source.
     *
     * @param context the context
     */
    public DataSource(Context context) {
        db = new databaseHelper(context);
    }



    /**
     * @return defCategory, the ContentValues to persist for the fallback/ default NoteCategory
     */
//    public static ContentValues getDefaultCategory() {
//        ContentValues defCategory = new ContentValues();
//        defCategory.put(databaseHelper.CATEGORY_NAME, NoteCategory.MAIN_NAME);
//        defCategory.put(databaseHelper.CATEGORY_COLOR_INT, NoteCategory.MAIN_COLOR);
//        return defCategory;
//    }

    /**
     * Open.
     *
     * @throws SQLException the sql exception
     */
    public void open() throws SQLException {
        database = db.getWritableDatabase();

    }

    /**
     * Close.
     */
    protected void close() {
        db.close();
    }

    /* ************************************** Note DataSource Methods ***************************************  */

    /**
     * Insert note boolean.
     *
     * @param note inserts new note into the database.
     * @return true for success and false for failure to insert note obj
     */
    public boolean insertNote(Notes note) {
        boolean didSucceed = false;
        database = db.getWritableDatabase();

        try {

            ContentValues contentValues = new ContentValues();

            ContentValues cv = new ContentValues();

            cv.put( databaseHelper.mynotes, note.getText());
            cv.put(databaseHelper.titles, note.getTitles());

           ;

            didSucceed = database.insert(databaseHelper.notes_table, null, contentValues) > 0;


        } catch (Exception ex) {

            Log.e(TAG, "@ insertNote() ", ex);
            ex.printStackTrace();

        }
        return didSucceed;
    }

    public boolean checkNotes() {
        int count = 0;
        try {
            String query = "SELECT _id FROM " + databaseHelper.notes_table;
            Cursor noteCursor = database.rawQuery(query, null);
            count = noteCursor.getCount();
            noteCursor.close();

        } catch (Exception e) {
            Log.e(TAG, "@ checkNotes() ", e);
        }
        return count > 0;

    }

    /**
     * Update note boolean.

     */
    public void updateCourse(Notes notes) {

        // calling a method to get writable database.
        database = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //  passing all value along with its key and value pair.
        cv.put(databaseHelper.titles, notes.getTitles());
        cv.put(databaseHelper.mynotes, notes.getText());

        //  calling a update method to update our database and passing our values.

        database.update(databaseHelper.notes_table, cv,databaseHelper.noteid+ "	= ?", new String[] { String.valueOf(Notes.getId())});
        database.close();
    }


    /**
     * Gets last note id.
     *
     * @return the ID aka.'noteID', of the last note to be inserted into the database
     */
    public int getLastNoteId() {
        int lastId = -1;
        try {
            String query = "Select MAX(" + databaseHelper.noteid + ") from " + databaseHelper.notes_table;
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();

        } catch (Exception e) {
            lastId = -1;
            Log.e(TAG, "@ getLastNoteId()");
        }
        return lastId;
    }

    /**
     * Gets notes.

     * @return the notes
     */
    public ArrayList<Notes> getNotes(String sortField, String sortOrder) {
        ArrayList<Notes> notes = new ArrayList<>();

        try {
            String noteQuery = "SELECT * FROM " + databaseHelper.notes_table + " ORDER BY " + sortField + " " + sortOrder;
            Cursor noteCursor = database.rawQuery(noteQuery, null);
            Notes note;

            noteCursor.moveToFirst(); //Move to first

            while (!noteCursor.isAfterLast()) {

                note = new Notes();
//                int noteID = noteCursor.getInt(0);
//                note.setNoteID(noteID);
                note.setText(noteCursor.getString(1));
                note.setTitles(noteCursor.getString(2));


                notes.add(note);
                noteCursor.moveToNext();
            }
            noteCursor.close();

        } catch (Exception e) {
            notes = new ArrayList<>();
            Log.e(TAG, "@ getNote() ", e);


        }
        return notes;

    }

    /**
     * Gets specific note.
     *
     * @param noteID the note id
     * @return the specific note
     */
    public Notes getSpecificNote(int noteID) {
        Notes note = new Notes();

        String query = "SELECT * FROM " + databaseHelper.notes_table + " WHERE " + databaseHelper.noteid + "=" + noteID;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            note.setText(cursor.getString(1));
            note.setTitles(cursor.getString(2));

            cursor.close();
        }

        return note;

    }



    /**
     * Delete Note.
     *
     * @param noteId the id of the note to delete
     */
    public void delete(int noteId) throws Exception {
        boolean didDelete = false;
        didDelete = database.delete(databaseHelper.notes_table, databaseHelper.noteid + "=" + noteId, null) > 0;
        if (!didDelete) {
            throw new Exception("Something went wrong while trying to delete the note from the database :(");
        }
    }


}