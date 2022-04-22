package com.example.reactiontest.ui.game;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.reactiontest.data.GameRepository;
import com.example.reactiontest.data.model.UserScore;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameViewModel extends ViewModel {

    GameRepository repository;
    private final MutableLiveData<Boolean> isGameRunning = new MutableLiveData<Boolean>(false);
    private final MutableLiveData<Boolean> isReacting = new MutableLiveData<>(false);
    private final MutableLiveData<GameResult> result = new MutableLiveData<>();
    private final MutableLiveData<Long> currentBestScore = new MutableLiveData<Long>(0L);

    private long reactStartTime;
    private long reactEndTime;

    Timer reactStartTimer;

    String currentUser;

    public MutableLiveData<Boolean> getIsGameRunning() {
        return isGameRunning;
    }

    public MutableLiveData<Boolean> getIsReacting() {
        return isReacting;
    }

    public MutableLiveData<GameResult> getResult() {

        return result;
    }

    public MutableLiveData<Long> getCurrentBestScore()
    {
        return currentBestScore;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;

        try
        {
            currentBestScore.setValue(repository.getBestUserScore(currentUser).getScore());
        }
        catch (Exception e)
        {
            currentBestScore.setValue(0L);
        }
    }

    public GameViewModel(GameRepository repository)
    {
        this.repository = repository;
    }

    public void setGame()
    {
        if(!isGameRunning.getValue())
        {
            startGame();
        }
        else
        {
            react();
        }
    }

    private void startGame()
    {
        isGameRunning.setValue(true);

        reactStartTimer = new Timer();

        reactStartTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isReacting.postValue(true);
                reactStartTime = System.currentTimeMillis();
            }
        }, new Random().nextInt(10000) + 500);
    }

    private void react()
    {
        long score = Long.MAX_VALUE;
        boolean isBest = false;
        reactStartTimer.cancel();

        if(isReacting.getValue())
        {
            reactEndTime = System.currentTimeMillis();

            score = reactEndTime - reactStartTime;

            long best = getBestScore().getScore();

            if(best > score || best == -1)
            {
                isBest = true;
                currentBestScore.postValue(score);
                recordScore(score);
            }
        }

        result.setValue(new GameResult(score, isBest));

        isReacting.setValue(false);
        isGameRunning.setValue(false);
    }

    private UserScore getBestScore()
    {
        UserScore result;

        try
        {
            result = repository.getBestUserScore(currentUser);
        }
        catch (Exception e)
        {
            result = new UserScore(currentUser, -1);
        }

        return result;
    }

    private void recordScore(long score)
    {
        try
        {
            repository.addUserScore(new UserScore(currentUser, score));
        }
        catch (Exception ignored) {}
    }
}
