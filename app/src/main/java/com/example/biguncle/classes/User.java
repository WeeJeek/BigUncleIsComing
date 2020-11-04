package com.example.biguncle.classes;

public class User {

    //attributes
    private String _name;
    private boolean _gender;
    private int balance;

    //constructor
    public User(String name, boolean my_gender){
        this._name = name;
        this._gender = my_gender;
    }

    public String get_name(){
        return this._name;
    }

    public boolean get_gender(){
        return this._gender;
    }




}
