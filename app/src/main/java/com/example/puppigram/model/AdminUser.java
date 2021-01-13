package com.example.puppigram.model;

public class AdminUser extends AbstractUser{
    public AdminUser(int id, String name, String gender, String mail, String hash_password) {
        super(id, name, gender, mail, hash_password);
    }

    @Override
    public boolean check_password(String password) {
        return false;
    }
}
