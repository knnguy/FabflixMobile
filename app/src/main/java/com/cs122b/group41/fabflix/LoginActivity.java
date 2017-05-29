package com.cs122b.group41.fabflix;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cs122b.group41.fabflix.Constants.URLContants;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private static final String TAG = "LoginActivity";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin(View view) {
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered an email
        if (email.isEmpty()) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        // Check if the user entered a password
        if (password.isEmpty()) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            validateUserWithBackend(email, password);
        }
    }

    private void validateUserWithBackend(String username, String password){
        final Map<String, String> params = new HashMap<>();
        final String currentUsername = username;
        final String currentPassword = password;

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = URLContants.BASE_PROD_SERVER_URL + "/login";

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        validateResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                params.put("username", currentUsername);
                params.put("password", currentPassword);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void validateResponse(String serverResponse) {
        if (serverResponse.equals("success")) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, R.string.error_invalid_user, Toast.LENGTH_SHORT);
            toast.show();
            mEmailView.requestFocus();
        }
    }

}

