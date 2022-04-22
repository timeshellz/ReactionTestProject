package com.example.reactiontest.data.model;

public class UserScore {
    protected String username;
    protected long score;

    public UserScore(String username, long score)
    {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public long getScore() {
        return score;
    }

    @Override
    public String toString()
    {
        return "User: " + username + ". Best time: " + score + " ms.";
    }
}
