package com.example.biguncle.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.example.biguncle.R;
import com.example.biguncle.classes.User;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    boolean is_male;
    CheckBox cb_male;
    CheckBox cb_female;
    EditText usernameEditText;
    User user;
    Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        loginButton = findViewById(R.id.btn_register);
        cb_male = findViewById(R.id.cb_male);
        cb_female = findViewById(R.id.cb_female);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }

            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);

        cb_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_male = true;
                switch_status_of_checkbox();
            }
        });

        cb_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_male = false;
                switch_status_of_checkbox();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = usernameEditText.getText().toString();
                if(is_username_decided() && is_one_of_checkbox_checked()){
                    user = new User(user_name, is_male);
                    Toast toast = Toast.makeText(getApplicationContext(), "user created", Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "user not created", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private boolean is_one_of_checkbox_checked(){
        if(cb_male.isChecked() || cb_female.isChecked()){
            return true;
        }
        return false;
    }

    private boolean is_username_decided(){
        if(usernameEditText.getText().toString() != "")
            return true;
        return false;
    }

    private void switch_status_of_checkbox(){
        cb_male.setChecked(is_male);
        cb_female.setChecked(!is_male);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}