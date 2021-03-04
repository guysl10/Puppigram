package com.example.puppigram.model;


public class Comment {
    /**
     * @param content: what the user comment.
     * @param user_id: User wrote the comment.
     * @
     */
    public int content;
    public int user_id;


    public Comment(int content, int user_id){
        this.content = content;
        this.user_id = user_id;
    }
}
