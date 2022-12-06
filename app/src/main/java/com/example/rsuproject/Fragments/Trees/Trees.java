package com.example.rsuproject.Fragments.Trees;

public class Trees {
    private int id;
    private String name;
    private String birth_String;
    private String planting_String;
    private String description;
    private String latitude;
    private String longitude;

    private int species_id;
    private int zone_id;
    private int user_id;

    private String family_name;
    private String species_name;

    public Trees(int id, String name, String birth_String, String planting_String, String description, String latitude, String longitude, int species_id, int zone_id, int user_id, String family_name, String species_name) {
        this.id = id;
        this.name = name;
        this.birth_String = birth_String;
        this.planting_String = planting_String;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.species_id = species_id;
        this.zone_id = zone_id;
        this.user_id = user_id;
        this.family_name = family_name;
        this.species_name = species_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth_String() {
        return birth_String;
    }

    public String getPlanting_String() {
        return planting_String;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getSpecies_id() {
        return species_id;
    }

    public int getZone_id() {
        return zone_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getSpecies_name() {
        return species_name;
    }
}
