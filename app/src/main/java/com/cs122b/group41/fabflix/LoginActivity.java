package com.cs122b.group41.fabflix;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    private boolean isValidUser = false;

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
        Log.i("response", "Attempt Login");

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered an email
        if (email.isEmpty()) {

            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        // Check if the user entered a password
        if (password.isEmpty()) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            connectToTomcat(email, password);
            if (isValidUser) {
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
            }
            else {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, R.string.error_invalid_user, Toast.LENGTH_SHORT);
                toast.show();
                focusView = mEmailView;
                focusView.requestFocus();
            }
        }
    }

    private void connectToTomcat(String username, String password){
        final Map<String, String> params = new HashMap<String, String>();
        final String currentUsername = username;
        final String currentPassword = password;

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final Context context = getApplicationContext();
        String url = "http://128.195.52.58:8080/TomcatTest/servlet/TomcatTest";

        // Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        validateUser(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                params.put("username",currentUsername);
                params.put("password",currentPassword);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return ;
    }

    private void validateUser(String serverResponse) {
        if (serverResponse.equals("success")) {
            isValidUser = true;
        }
    }

}

