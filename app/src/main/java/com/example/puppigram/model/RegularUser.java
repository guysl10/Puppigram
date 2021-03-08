package com.example.puppigram.model;

/**
 * Represent a simple user.
 */
public class RegularUser extends AbstractUser{
    public RegularUser(int id, String name,
                       String gender, String mail,
                       String hash_password, String image_path
    ){
        super(id, name, gender, mail, hash_password, image_path);
    }

    @Override
    public boolean check_password(String password) {
        return false;
    }
}
