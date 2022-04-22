package com.example.reactiontest.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.reactiontest.data.LoginRepository;
import com.example.reactiontest.data.Result;
import com.example.reactiontest.R;
import com.example.reactiontest.data.model.User;

import java.util.concurrent.ExecutionException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        User user = null;

        try
        {
            user = loginRepository.getUser(username);
        }
        catch (Exception ignored){}

        if (user != null && user.passwordsMatch(password)) {
            loginResult.setValue(new LoginResult(new LoggedInUserView(user.getUserName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));

            if(user == null)
            {
                try
                {
                    signup(username, password);
                    login(username, password);
                }
                catch (Exception ignored) { }
            }
        }
    }

    public void signup(String username, String password) throws ExecutionException, InterruptedException {
        loginRepository.addUser(new User(0, username, password));
        loginResult.setValue(new LoginResult(R.string.signed_up));
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}