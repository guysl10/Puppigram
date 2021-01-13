package com.example.puppigram.model;

public abstract class AbstractUser {
    public int id;
    public String name;
    public String gender;
    public String mail;
    private String _hash_password;
    //TODO: Add image..

    public AbstractUser(int id, String name, String gender, String mail, String hash_password){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.mail = mail;
        this._hash_password = hash_password;

    }
    public abstract boolean check_password(String password);
}
