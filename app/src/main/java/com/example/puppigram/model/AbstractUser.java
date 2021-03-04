package com.example.puppigram.model;

/**
 * Represent a general user.
 */
public abstract class AbstractUser {
    /**
     * @param id: User id.
     * @param name: Name of user.
     * @param gender: male/female/other
     * @param mail: Mail address of user.
     * @param hash_password: Password after hash of the user.
     * @param image_path: Path to user profile image.
     */
    public int id;
    public String name;
    public String gender;
    public String mail;
    private String _hash_password;
    public String image_path;

    public AbstractUser(int id, String name,
                        String gender, String mail,
                        String hash_password, String image_path
    ){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.mail = mail;
        this._hash_password = hash_password;
        this.image_path = image_path;
    }
    public abstract boolean check_password(String password);
}
