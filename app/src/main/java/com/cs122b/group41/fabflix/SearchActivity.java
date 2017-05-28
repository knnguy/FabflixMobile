package com.cs122b.group41.fabflix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cs122b.group41.fabflix.Constants.URLContants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = (EditText) findViewById(R.id.edit_text_search);
    }

    public void searchByTitle(View view) {
        String searchQuery = searchEditText.getText().toString();

        View focusView = searchEditText;

        if (searchQuery.isEmpty()) {
            searchEditText.setError(getString(R.string.error_field_required));
            focusView.requestFocus();
            return;
        }

        sendQueryToBackend(searchQuery);
    }

    private void sendQueryToBackend(String searchQuery) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = URLContants.BASE_DEV_SERVER_URL + "/autocompletesearch?fttitle=" + searchQuery;

        StringRequest searchRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        validateResponse(response);
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
                }
        );

        queue.add(searchRequest);
    }

    private void validateResponse(String serverResponse) {
        try {
            JSONObject jsonResponse = new JSONObject(serverResponse);
            if (jsonResponse.get("status").equals("success")) {
                if (jsonResponse.getJSONArray("results").length() == 0) {
                    Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SearchResultsActivity.class);
                    intent.putExtra("search_results", serverResponse);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Server is not responding", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_SHORT).show();
        }
    }
}
