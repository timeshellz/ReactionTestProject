package com.example.reactiontest.data;

import android.util.Log;

import com.example.reactiontest.data.model.DBHelper;
import com.example.reactiontest.data.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginRepository{

    private static volatile LoginRepository instance;
    private static DBHelper db;

    private LoginRepository() {
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            db = new DBHelper(AndroidApplication.getAppContext());
            instance = new LoginRepository();
        }
        return instance;
    }

    public User getUser(String userName) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<User> future = executor.submit(() ->
        {
            return db.getUser(userName);
        });

        return future.get();
    }

    public boolean addUser(User user) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future future = executor.submit(() ->
        {
            try {
                if(getUser(user.getUserName()) == null)
                    db.addUser(user.getUserName(), user.getPassword());

            } catch (Exception e) { return; }
        });
        future.get();
        return true;
    }
}