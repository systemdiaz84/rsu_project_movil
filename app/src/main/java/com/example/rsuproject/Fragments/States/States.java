package com.example.rsuproject.Fragments.States;

public class States {
    private int id;
    private String name,description;

    public States(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
