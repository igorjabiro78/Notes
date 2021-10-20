package com.example.notes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.ReminderDatabase;

import java.util.ArrayList;

public class reminderAdapter extends RecyclerView.Adapter<reminderAdapter.viewHolder> {

    Context context;
    Fragment ReminderFragment;
    ArrayList<ReminderClass> arrayList;
    ReminderDatabase reminderdatabase;

    public reminderAdapter(Context context, ReminderFragment RemFragment, ArrayList<ReminderClass> arrayList) {
        this.context = context;
        this.ReminderFragment  = RemFragment ;
        this.arrayList = arrayList;
    }

    @Override
    public reminderAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.reminder_text_view, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final reminderAdapter.viewHolder holder, final int position) {
        holder.Event.setText(arrayList.get(position).getEvent());
        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        reminderdatabase = new ReminderDatabase(context);

        holder.delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting note
                reminderdatabase.delete(Integer.toString(arrayList.get(position).getId()));
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView Event, date,time;
        ImageView delete, edit;
        public viewHolder(View itemView) {
            super(itemView);
            Event = (TextView) itemView.findViewById(R.id.eventName);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.EventTime);
//            delete = (ImageView) itemView.findViewById(R.id.delete);
//            edit = (ImageView) itemView.findViewById(R.id.edit);
        }
    }

    public void showDialog(final int pos) {
        final EditText message,date, time;
        Button setNotification;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.activity_date_picker);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        message = (EditText) dialog.findViewById(R.id.textmessage);
        date = (EditText) dialog.findViewById(R.id.date);
        time = (EditText) dialog.findViewById(R.id.et_time);
        setNotification = (Button) dialog.findViewById(R.id.set);

        message.setText(arrayList.get(pos).getEvent());
        date.setText(arrayList.get(pos).getDate());
        time.setText(arrayList.get(pos).getTime());

        setNotification.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    message.setError("Please Enter Title");
                }else if(date.getText().toString().isEmpty()|| time.getText().toString().isEmpty()) {
                    date.setError("Please Enter date ");
                    time.setError("please Enter time");
                }else {
                    //updating note
                    reminderdatabase.updateReminder(message.getText().toString(),date.getText().toString(), time.getText().toString(), Integer.toString(arrayList.get(pos).getId()));
                    arrayList.get(pos).setEvent(message.getText().toString());
                    arrayList.get(pos).setDate(date.getText().toString());
                    arrayList.get(pos).setTime(time.getText().toString());
                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }
}
