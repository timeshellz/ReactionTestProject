package com.example.reactiontest.ui.leaderboard;

import android.os.Build;

import androidx.lifecycle.ViewModel;

import com.example.reactiontest.data.GameRepository;
import com.example.reactiontest.data.model.UserScore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LeaderboardViewModel extends ViewModel {

    GameRepository repository;

    public LeaderboardViewModel(GameRepository repository)
    {
        this.repository = repository;
    }

    public List<UserScore> getUserScores()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return repository.getUserScores();
            }
        }
        catch (Exception ignored) {}

        return new ArrayList<>();
    }
}
