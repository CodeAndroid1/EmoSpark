/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.services;

import android.os.AsyncTask;


public class DummyLoginService extends AsyncTask<Void, Void, Boolean> implements LoginService {

    /**
     * A dummy authentication store containing known user names and passwords.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "test@icinetic.com:icinetic"};

    private String mEmail;

    private String mPassword;

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(mEmail)) {
                // Account exists, return true if the password matches.
                return pieces[1].equals(mPassword);
            }
        }

        return true;
    }


    @Override
    public void attemptLogin(String email, String password) {
        mEmail = email;
        mPassword = password;
        this.execute();
    }
}

