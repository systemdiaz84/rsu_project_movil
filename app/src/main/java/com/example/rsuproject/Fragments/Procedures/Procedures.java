package com.example.rsuproject.Fragments.Procedures;

public class Procedures {

    private int id, procedure_type_id, tree_id, responsible_id, user_id;
    private String date, description,procedure_type_name ;

    public Procedures(String date, String procedure_type_name) {
        this.date = date;
        this.procedure_type_name = procedure_type_name;
    }

    public String getDate() {
        return date;
    }

    public String getProcedure_type_name() {
        return procedure_type_name;
    }
}
