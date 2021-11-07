package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterButton = (Button)findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> StartFilterSearch());
    }

    private void StartFilterSearch() {
        Intent intent = new Intent(this, FilterSearchActivity.class);
        startActivity(intent);
    }


}