package com.example.reactiontest.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.reactiontest.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(@Nullable Context context) {
        super(context, "db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE userScore (username TEXT PRIMARY KEY, score INTEGER DEFAULT -1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS userScore");

        onCreate(sqLiteDatabase);
    }

    public void addUser(String username, String password)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        sqLiteDatabase.insert("user", null, contentValues);
        sqLiteDatabase.close();
    }

    public User getUser(String username)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("SELECT _id, username, password FROM user WHERE username = '" + username + "'", null);

        User result = null;
        if (c.moveToFirst()){
            result = new User(c.getInt(0), c.getString(1), c.getString(2));
        }
        sqLiteDatabase.close();
        c.close();
        return result;
    }

    public UserScore getBestUserScore(String username)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("SELECT username, score FROM userScore WHERE username = '" + username + "'", null);

        UserScore result = new UserScore(username, -1);
        if (c.moveToFirst()){
            result = new UserScore(c.getString(0), c.getLong(1));
        }
        sqLiteDatabase.close();
        c.close();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<UserScore> getUserScores()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery("SELECT username, score FROM userScore", null);

        List<UserScore> result = new ArrayList<>();
        if (c.moveToFirst()){

            do{
                result.add(new UserScore(c.getString(0), c.getLong(1)));
            }
            while (c.moveToNext());
        }

        result.sort((s1, s2) ->
        {
            if(s1.score < s2.score)
                return -1;

            if(s1.score > s2.score)
                return 1;

            return 0;
        });

        sqLiteDatabase.close();
        c.close();
        return result;
    }

    public void updateUserScore(UserScore score)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", score.getUsername());
        contentValues.put("score", score.getScore());

        int id = (int)sqLiteDatabase.insertWithOnConflict("userScore", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        if (id == -1)
            sqLiteDatabase.update("userScore", contentValues, "username=?", new String[] {score.getUsername()});
        sqLiteDatabase.close();
    }
}
