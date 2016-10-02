/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Service;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.services.LoginService;
import ibmmobileappbuilder.util.SecurePreferences;

/**
 * A login screen that offers login via email/password.
 */
public abstract class BaseLoginActivity extends BaseActivity {

    private String mEmail;

    private String mPassword;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private boolean loginTaskRunning = false;

    private SecurePreferences mSharedPreferences;

    // UI references.
    private EditText mEmailView;

    private EditText mPasswordView;

    private View mProgressView;

    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //hide the soft keyboard programatically when the button is pressed
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailSignInButton.getWindowToken(), 0);
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void populateAutoComplete() {
        mEmailView.setText(mSharedPreferences.getString("lastUser", null));
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (loginTaskRunning) {
            return;
        }
        //set up the Login service
        LoginService loginService = createLoginService();

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPassword) && !isPasswordValid(mPassword)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(mEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            loginTaskRunning = true;
            loginService.attemptLogin(mEmail, mPassword);
        }
    }

    abstract public LoginService createLoginService();

    /**
     * Override this with your own email validation logic
     *
     * @param email the email to validate
     * @return true if email is valid
     */
    public boolean isEmailValid(String email) {
        return true;
    }

    /**
     * Override this with your own password validation logic
     *
     * @param password the password to validate
     * @return true if password is valid
     */
    public boolean isPasswordValid(String password) {
        return true;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setLoginTaskRunning(boolean loginTaskRunning) {
        this.loginTaskRunning = loginTaskRunning;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    public void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public SecurePreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public void setSharedPreferences(SecurePreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }
}



