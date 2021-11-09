package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;

public class deleteAdapter extends RecyclerView.Adapter<deleteAdapter.MyViewHolder> {

    private ArrayList<Notes> mynotes = new ArrayList<>();
    private JSONArray deletednotes;
    public LinearLayout ln;
    public Context contx;
    public databaseHelper mdatabase;
    RecyclerView rv;

public static class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView TvId,Tvtext,Tvtitle, Tvdate, Tvstat;

//        public LinearLayout lnProduct;


    public MyViewHolder(LinearLayout lnout) {
        super(lnout);
//            lnProduct = lnout.findViewById(R.id.productLinear);
        TvId= lnout.findViewById(R.id.id);
        Tvtext = lnout.findViewById(R.id.deletedText);
        Tvtitle = lnout.findViewById(R.id.deletedTitle);
        Tvdate = lnout.findViewById(R.id.date);
        Tvstat = lnout.findViewById(R.id.status);

    }
}

//
    public deleteAdapter(Context context, ArrayList<Notes> notesList, RecyclerView rv) {

        contx = context; // context of deleteAdapter
        this.mynotes = notesList;
        this.rv = rv;

    }
    public deleteAdapter(ArrayList<Notes> arrayList){
    mynotes = arrayList;
    }
    @Override
    public deleteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        ln = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deleted_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(ln);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

            Notes notes= mynotes.get(position);

            holder.Tvtitle.setText(notes.getTitles()); // object.getString is used when you are fetching from serverfu
            holder.Tvtext.setText(notes.getText()); // object.getString is used when you are fetching from serverfu
            holder.Tvstat.setText(notes.getSync());
            holder.Tvdate.setText((CharSequence) mdatabase.created_date);


            holder.Tvtext.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Create the object of
                    // AlertDialog Builder class
                    AlertDialog.Builder builder  = new AlertDialog
                            .Builder(contx);

                    // Set the message show for the Alert time
                    builder.setMessage("Make changes to this notes "+holder.Tvtitle.getText().toString());

                    // Set Alert Title
                    builder.setTitle("Recover or permanent delete note ");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(true);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder.setPositiveButton("Recover",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            // When the user click yes button
                            // then app will close
                            add_notes.recoverdeleteNote(contx,holder.TvId.getText().toString());
                            dialog.dismiss();
                        }
                    });

                    // Set the Negative button with No name
                    // OnClickListener method is use
                    // of DialogInterface interface.
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            // If user click no
                            // then dialog box is canceled.
                            dialog.cancel();

                        }
                    });
                    builder.setNegativeButton("Delete",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            // When the user click yes button
                            // then app will close
                            add_notes.permanentlydeleteNote(contx,holder.TvId.getText().toString());
                            dialog.dismiss();
                        }
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                    return true;
                }

            });


        }




    @Override
    public int getItemCount() {

        return mynotes.size();
    }
}

