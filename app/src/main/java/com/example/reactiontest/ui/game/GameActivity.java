package com.example.reactiontest.ui.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.reactiontest.R;
import com.example.reactiontest.ui.ViewModelFactory;
import com.example.reactiontest.ui.leaderboard.LeaderboardActivity;

public class GameActivity extends AppCompatActivity {

    TextView userView;
    TextView tutorialView;
    TextView tutorialViewAdditional;
    TextView scoreView;
    GameViewModel gameViewModel;
    Button startButton;
    Button leaderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        userView = findViewById(R.id.usernameView);
        userView.setText(getIntent().getStringExtra("username"));

        tutorialView = findViewById(R.id.textView2);
        tutorialViewAdditional = findViewById(R.id.textView3);

        startButton = findViewById(R.id.button);
        leaderButton = findViewById(R.id.leaderboardButton);

        scoreView = findViewById(R.id.bestScoreView);

        gameViewModel = new ViewModelProvider(this, new ViewModelFactory())
                .get(GameViewModel.class);

        gameViewModel.setCurrentUser(getIntent().getStringExtra("username"));

        gameViewModel.getIsGameRunning().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(aBoolean)
                        {
                            tutorialViewAdditional.setVisibility(View.INVISIBLE);
                            tutorialView.setVisibility(View.INVISIBLE);
                            startButton.setText("PAY ATTENTION");
                            startButton.setBackgroundColor(getResources().getColor(R.color.yellow));
                        }
                        else
                        {
                            GameResult result = gameViewModel.getResult().getValue();

                            if(result != null)
                                createPopup(result);

                            tutorialViewAdditional.setVisibility(View.VISIBLE);
                            tutorialView.setVisibility(View.VISIBLE);
                            startButton.setText("START");
                            startButton.setBackgroundColor(getResources().getColor(R.color.red));
                        }
                    }
                });
            }
        });

        gameViewModel.getIsReacting().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(aBoolean)
                        {
                            startButton.setText("TAP!");
                            startButton.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                    }
                });
            }
        });

        gameViewModel.getCurrentBestScore().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(aLong != -1)
                    scoreView.setText(aLong + " ms");
                else
                    scoreView.setText("N/A");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameViewModel.setGame();
            }
        });

        leaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLeaderboard();
            }
        });
    }

    private void createPopup(GameResult result)
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.result_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        TextView descriptionView = popupView.findViewById(R.id.resultDescrtiptionView);

        String description;

        if(result.getScore() == Long.MAX_VALUE)
            description = "You tapped too soon!";
        else
        {
            description = "Score: " + result.getScore() + "ms.";

            if(result.isBest())
                description += "\nNEW RECORD!";
        }

        descriptionView.setText(description);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void openLeaderboard()
    {
        Intent leaderboardIntent = new Intent(GameActivity.this, LeaderboardActivity.class);
        startActivity(leaderboardIntent);
    }
}