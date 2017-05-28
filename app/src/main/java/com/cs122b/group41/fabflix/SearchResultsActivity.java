package com.cs122b.group41.fabflix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultsActivity";
    private static final int RESULTS_PER_PAGE = 10;

    private ListView resultsListView;
    private Button prevButton;
    private Button nextButton;

    private ArrayList<String> movieNamesList;
    private int resultOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        resultsListView = (ListView) findViewById(R.id.listview_results);
        prevButton = (Button) findViewById(R.id.button_prev);
        nextButton = (Button) findViewById(R.id.button_next);

        Bundle bundle = getIntent().getExtras();
        try {
            JSONObject jsonResults = new JSONObject((String) bundle.get("search_results"));
            JSONArray movieJsonObjects = (JSONArray) jsonResults.get("results");
            movieNamesList = new ArrayList<>();
            for (int i = 0; i < movieJsonObjects.length(); i++) {
                movieNamesList.add(movieJsonObjects.getJSONObject(i).getString("title"));
            }
        } catch (JSONException e) {
            /* do nothing */
        }

        resultsListView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                movieNamesList.subList(resultOffset, Math.min(movieNamesList.size(), resultOffset + RESULTS_PER_PAGE)))
        );

        if (resultOffset + RESULTS_PER_PAGE >= movieNamesList.size()) {
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    public void viewPrevResults(View view) {
        resultOffset = Math.max(0, resultOffset - RESULTS_PER_PAGE);
        resultsListView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                movieNamesList.subList(resultOffset, Math.min(movieNamesList.size(), resultOffset + RESULTS_PER_PAGE)))
        );
        setButtonsVisibility();
    }

    public void viewNextResults(View view) {
        resultOffset += RESULTS_PER_PAGE;
        resultsListView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                movieNamesList.subList(resultOffset, Math.min(movieNamesList.size(), resultOffset + RESULTS_PER_PAGE)))
        );
        setButtonsVisibility();
    }

    private void setButtonsVisibility() {
        if (resultOffset == 0) {
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
        }

        if (resultOffset + RESULTS_PER_PAGE >= movieNamesList.size()) {
            nextButton.setVisibility(View.GONE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }
}
