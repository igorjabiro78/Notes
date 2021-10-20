package com.example.notes;

public class ReminderClass {
    private int id;
    private String Event;
    private String date;
    private String time;

    public ReminderClass(String Event, String date, String time) {
        this.Event = Event;
        this.date = date;
        this.time = time;
    }

    public ReminderClass() {
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }


    public String getEvent() {
        return Event;
    }

    public void setEvent(String Event) {
        this.Event = Event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ReminderClass{" + "id=" + id + ", Event=" + Event + ", date=" + date + ", time=" + time + '}';
    }

}