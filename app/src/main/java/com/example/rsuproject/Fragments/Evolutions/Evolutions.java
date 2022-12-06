package com.example.rsuproject.Fragments.Evolutions;

public class Evolutions {
    private int id, tree_id;
    private String height, width, date;

    public Evolutions(int id, int tree_id, String height, String width, String date) {
        this.id = id;
        this.tree_id = tree_id;
        this.height = height;
        this.width = width;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getTree_id() {
        return tree_id;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getDate() {
        return date;
    }
}
