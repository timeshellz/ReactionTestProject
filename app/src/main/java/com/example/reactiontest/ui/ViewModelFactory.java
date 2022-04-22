package com.example.reactiontest.ui;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.reactiontest.data.GameRepository;
import com.example.reactiontest.data.LoginRepository;
import com.example.reactiontest.ui.game.GameViewModel;
import com.example.reactiontest.ui.leaderboard.LeaderboardViewModel;
import com.example.reactiontest.ui.login.LoginViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(LoginRepository.getInstance());
        }
        else if (modelClass.isAssignableFrom(GameViewModel.class)) {
            return (T) new GameViewModel(GameRepository.getInstance());
        }
        else if (modelClass.isAssignableFrom(LeaderboardViewModel.class)) {
            return (T) new LeaderboardViewModel(GameRepository.getInstance());
        }
        else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}