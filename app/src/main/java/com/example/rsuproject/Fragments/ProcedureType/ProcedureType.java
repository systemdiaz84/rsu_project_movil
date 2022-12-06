package com.example.rsuproject.Fragments.ProcedureType;

public class ProcedureType {
    private int id;
    private String name, description;

    public ProcedureType(int id, String name, String description) {
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
