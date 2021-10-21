package com.example.notes;

public class Notes {
//    private static int id;
    private String titles;
    private String Text;
    private int sync;

    public Notes() {

    }


    public Notes(String titles, String Text,int sync_status) {

        this.titles = titles;
        this.Text = Text;
        sync= sync_status;
    }

//    public static int getId() {
//        return id;
//    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getText() {
        return Text;
    }
    public int getSync() {
        return sync;
    }
    public void setSync(int sync_stat){
        sync = sync_stat;
    }

    public void setText(String Text) {
        this.Text = Text;
    }
    public void SetAll( String titles, String Text){
//        this.id = id;
        this.titles = titles;
        this.Text = Text;
    }

    @Override
    public String toString() {
        return "Notes{" +  " titles=" + titles + ", Text=" + Text + '}'+"\n";
    }
}
