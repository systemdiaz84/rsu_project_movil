package com.example.rsuproject.Fragments.Zones;

public class Zones {

    private int id;
    private String name;
    private String area;
    private String description;

    public Zones(int id, String name, String area, String description) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getDescription() {
        return description;
    }
}
