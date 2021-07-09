package com.example.notes;

public class Notes {
    private static int id;
    private String titles;
    private String Text;

    public Notes() {

    }


    public Notes(int id, String titles, String Text) {
        this.id = id;
        this.titles = titles;
        this.Text = Text;
    }

    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }
    public void SetAll(int id, String titles, String Text){
        this.id = id;
        this.titles = titles;
        this.Text = Text;
    }

    @Override
    public String toString() {
        return "Notes{" + "id=" + id + ", titles=" + titles + ", Text=" + Text + '}'+"\n";
    }
}
