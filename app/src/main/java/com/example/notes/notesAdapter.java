package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.databaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class notesAdapter extends RecyclerView.Adapter<notesAdapter.MyViewHolder> { // adapter idufasha gu settinga content kuma recycle view

    List<Notes> mynotes;
    RecyclerView rv;
    private JSONArray savednotes;
    public LinearLayout ln;
    public Context contx;
    public databaseHelper mdatabase;
   // public SQLiteDatabase mDatabase;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView Tvtext, Tvtitle, Tvdate, Tvstat, tvId;
        public LinearLayout lnyNotes;

        public MyViewHolder(LinearLayout lnout) {
            super(lnout);
            tvId = lnout.findViewById(R.id.itemId);
            Tvtext = lnout.findViewById(R.id.Text);
            Tvtitle = lnout.findViewById(R.id.Title);
            Tvdate = lnout.findViewById(R.id.date);
            Tvstat = lnout.findViewById(R.id.status);
            lnyNotes = lnout.findViewById(R.id.lnyNotes);



        }
    }

//    public notesAdapter(Context context, JSONArray notedata) {
//        savednotes = notedata; // ibika data twoherej kuri adapter
//        contx = context; // context of notesAdapter
////        mDatabase = new SQLiteDatabase(context);
//
//    }

    public notesAdapter(Context context, List<Notes>notesList,RecyclerView rv) {

        contx = context; // context of notesAdapter
        this.mynotes = notesList;
        this.rv = rv;

//        mDatabase = new SQLiteDatabase(context);

    }

    @Override
    public notesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        ln = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(ln);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        try {
            Notes notes = mynotes.get(position);
            JSONObject object = savednotes.getJSONObject(position);
            holder.tvId.setText(object.getString("id")); // object.getString is used when you are fetching from serverfu
            holder.Tvtitle.setText(object.getString("Title")); // object.getString is used when you are fetching from serverfu
            final String textNote = object.getString("Text");
            final String newText = textNote.length()>80?textNote.substring(0,80)+"...":textNote;
            holder.Tvtext.setText(newText); // object.getString is used when you are fetching from serverfu
            holder.Tvstat.setText(object.getString("Status"));
            holder.Tvdate.setText(object.getString("created_at"));

            holder.Tvtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent = new Intent();
                    intent.putExtra("id",holder.tvId.getText().toString());
                    intent.putExtra("title",holder.Tvtitle.getText().toString());
                    intent.putExtra("date",holder.Tvdate.getText().toString());
                    intent.putExtra("text",textNote);

                    AlertDialog.Builder builder  = new AlertDialog
                            .Builder(contx);

                    // Set the message show for the Alert time
                    builder.setMessage("choose action "+holder.Tvtitle.getText().toString());

                    // Set Alert Title

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(true);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder.setPositiveButton("View",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            // When the user click yes button
                            // then app will close
                            intent.setClass(contx,ViewNotes.class);
                            contx.startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    // Set the Negative button with No name
                    // OnClickListener method is use
                    // of DialogInterface interface.
                    builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            intent.setClass(contx,Edit_Texts.class);
                            contx.startActivity(intent);
                            // If user click no
                            // then dialog box is canceled.

                            dialog.cancel();

                        }
                    });
                    builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            // If user click no
                            // then dialog box is canceled.
                            dialog.cancel();

                        }
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();


                }
            });

            holder.Tvtext.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder  = new AlertDialog
                            .Builder(contx);

                    // Set the message show for the Alert time
                    builder.setMessage("comfirm delete "+holder.Tvtitle.getText().toString());

                    // Set Alert Title
                    builder.setTitle("Delete note ");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(true);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // When the user click yes button
                                            // then app will close
                                           mdatabase.delete(holder.tvId.getText().toString());
                                            add_notes.TemporalydeleteNote(contx,holder.tvId.getText().toString());
                                            dialog.dismiss();
                                        }
                                    });

                    // Set the Negative button with No name
                    // OnClickListener method is use
                    // of DialogInterface interface.
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            // If user click no
                                            // then dialog box is canceled.
                                            dialog.cancel();

                                        }
                                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                    return true;
                }

            });
        } catch (JSONException ex) {

        }
    }
    @Override
    public int getItemCount() {

        return mynotes.size();
    }
}

