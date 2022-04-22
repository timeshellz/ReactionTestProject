package com.example.reactiontest.data;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.reactiontest.data.model.DBHelper;
import com.example.reactiontest.data.model.UserScore;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameRepository{
    private static volatile GameRepository instance;
    private static DBHelper db;

    private GameRepository() {
    }

    public static GameRepository getInstance() {
        if (instance == null) {
            db = new DBHelper(AndroidApplication.getAppContext());
            instance = new GameRepository();
        }
        return instance;
    }

    public UserScore getBestUserScore(String userName) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<UserScore> future = executor.submit(() ->
        {
            return db.getBestUserScore(userName);
        });
        return future.get();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<UserScore> getUserScores() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<UserScore>> future = executor.submit(() ->
        {
            return db.getUserScores();
        });
        return future.get();
    }

    public void addUserScore(UserScore userScore) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future future = executor.submit(() ->
        {
            db.updateUserScore(userScore);
        });
        future.get();
    }
}
