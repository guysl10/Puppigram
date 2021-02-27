package com.example.puppigram.model;


/**
 * Represent a Admin kind of user.
 * This user have full access to the application and can manage the application.
 */
public class AdminUser extends AbstractUser{
    public AdminUser(int id, String name, String gender, String mail, String hash_password) {
        super(id, name, gender, mail, hash_password, null);
    }

    @Override
    public boolean check_password(String password) {
        return false;
    }
}
