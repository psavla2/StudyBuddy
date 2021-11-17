package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button filterButton = (Button)findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> StartFilterSearch());

        Button searchButton = (Button)findViewById(R.id.name_search_button);
        searchButton.setOnClickListener(v -> StartSearchByName());
    }

    private void StartFilterSearch() {
        //Intent intent = new Intent(this, FilterSearchActivity.class);
        Intent intent = new Intent(this, LocationPageActivity.class);
        intent.putExtra("filter_tags", new Integer[] { 2, 3, 5 });
        startActivity(intent);
    }

    private void StartSearchByName() {
        Intent intent = new Intent(this, NameSearchActivity.class);
        startActivity(intent);
    }
}