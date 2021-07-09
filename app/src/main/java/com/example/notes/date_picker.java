package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.notes.Database_for_notification.DatabaseClass;
import com.example.notes.Database_for_notification.EntityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class date_picker extends AppCompatActivity implements View.OnClickListener {

    Button setnotification;
    String timeTonotify;
    private EditText etTime,etDate,message;

    DatabaseClass databaseClass;

    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        message = findViewById(R.id.textmessage);
        etTime = findViewById(R.id.et_time);
        etDate = findViewById(R.id.date);
        setnotification  = findViewById(R.id.set);

        setnotification.setOnClickListener(this);
        etTime.setOnClickListener(this);
        etDate.setOnClickListener(this);
        databaseClass = DatabaseClass.getDatabase(getApplicationContext());


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        setnotification.setOnClickListener(this);

//        etTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               DatePickerDialog datePickerDialog = new DatePickerDialog(
//                       date_picker.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
//                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                datePickerDialog.show();
//            }
//        });
      setListener = new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
              month = month+1;
              String date = day+"/"+month+"/"+year;
              etDate.setText(date);
          }
      };
      etDate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              DatePickerDialog datePickerDialog = new DatePickerDialog(date_picker.this, new DatePickerDialog.OnDateSetListener() {
                  @Override
                  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                      month = month+1;
                      String date = day+"/"+month+"/"+year;
                      etDate.setText(date);
                  }
              },year,month,day);
              datePickerDialog.show();

          }
      });
    }

    @Override
    public void onClick(View v) {
     if (v == etTime) {
            selectTime();
        } else if (v == etDate) {
            selectDate();
        } else if (v == setnotification){
         submit();
        }
    }

    private void submit() {
        String text = message.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Please Enter or record the text", Toast.LENGTH_SHORT).show();
        } else {
            if (etTime.getText().toString().equals("Select Time") || etDate.getText().toString().equals("Select date")) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            } else {
                EntityClass entityClass = new EntityClass();
                String value = (message.getText().toString().trim());
                String date = (etDate.getText().toString().trim());
                String time = (etTime.getText().toString().trim());
                entityClass.setEventdate(date);
                entityClass.setEventname(value);
                entityClass.setEventtime(time);
                databaseClass.EventDao().insertAll(entityClass);
                setAlarm(value, date, time);
            }
        }
    }


    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                etTime.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private String FormatTime(int hour, int minute) {
        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }

        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }

        return time;
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                etDate.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

//        SimpleDateFormat in = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
//        in.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));  //or Asia/Jerusalem
//        String s2 = "Fri Oct 23 11:07:08 IST 2015";
//        Date dates = in.parse(s2);
//
//        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        out.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
//        System.out.println(out.format(dates));


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
//        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        finish();

    }
}