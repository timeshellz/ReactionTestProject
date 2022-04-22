package com.example.reactiontest.ui.game;

import com.example.reactiontest.data.GameRepository;

public class GameResult {
    private long score;
    private boolean isBest;

    public long getScore() {
        return score;
    }

    public boolean isBest() {
        return isBest;
    }

    public GameResult(long score, boolean isBest)
    {
        this.score = score;
        this.isBest = isBest;
    }
}
