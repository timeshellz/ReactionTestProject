package com.example.reactiontest.data.model;

public class User {
    protected int Id;
    protected String userName;
    protected String password;

    public User(){}

    public int getId() {
        return Id;
    }

    public String getPassword() {
        return password;
    }

    public User(int id, String username, String password)
    {
        this.Id = id;
        this.userName = username;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public boolean passwordsMatch(String providedPassword)
    {
        if(providedPassword.equals(password))
            return true;

        return false;
    }
}
