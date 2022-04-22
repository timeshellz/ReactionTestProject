package com.example.reactiontest.ui.leaderboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ListAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.reactiontest.R;
import com.example.reactiontest.data.model.UserScore;
import com.example.reactiontest.ui.ViewModelFactory;
import com.example.reactiontest.ui.game.GameViewModel;

import java.lang.reflect.Array;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    ListView listView;
    Button returnButton;

    LeaderboardViewModel viewModel;
    List<UserScore> userScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        viewModel = new ViewModelProvider(this, new ViewModelFactory())
                .get(LeaderboardViewModel.class);

        listView = findViewById(R.id.scoreListView);
        returnButton = findViewById(R.id.closeLeaderboardButton);

        userScores = viewModel.getUserScores();
        ArrayAdapter<UserScore> adapter = new ArrayAdapter<UserScore>(this, R.layout.leaderboard_item, userScores);

        listView.setAdapter(adapter);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}