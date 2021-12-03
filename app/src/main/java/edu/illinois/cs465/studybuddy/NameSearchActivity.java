package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NameSearchActivity extends AppCompatActivity {

    private HashMap<Integer, StudySpace> mSpacesMap; // location ID -> StudySpace
    private List<LocationItem> mSortedSpacesList;
    private HashMap<Integer, Integer> mQueryScores; // location ID -> fitting alignment score

    private TextInputEditText mSearchField;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_search);

        mRecyclerView = findViewById(R.id.recycler_view);
        mSearchField = findViewById(R.id.search_field);

        mSpacesMap = new HashMap<>();
        mQueryScores = new HashMap<>();
        mSortedSpacesList = new ArrayList<>();

        StudySpace[] studySpaceArray = JsonReader.getSpaces(this);

        for (StudySpace s : studySpaceArray) {
            mSpacesMap.put(s.id, s);
            mQueryScores.put(s.id, 0);
            mSortedSpacesList.add(new LocationItem(s));
        }

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("text changed", String.format("changed to: %s", s.toString()));
                ExecuteSearch(s.toString());
            }
        });

        UpdateRecycler();
    }

    private void UpdateRecycler() {
        mRecyclerView.setHasFixedSize(true);
        /*mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        */
        int numberOfColumns = 2;
        mLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(mSortedSpacesList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private int CompareLocationItems(LocationItem a, LocationItem b) {
        Integer matchingA = mQueryScores.get(a.getId());
        Integer matchingB = mQueryScores.get(b.getId());
        matchingA = matchingA == null ? 0 : matchingA;
        matchingB = matchingB == null ? 0 : matchingB;
        return -1 * matchingA.compareTo(matchingB); // -1 indicates reverse order
    }

    private void ExecuteSearch(String query) {
        for (StudySpace s : mSpacesMap.values()) {
            mQueryScores.put(s.id, fittingAlign(query, s.name));
        }

        Collections.sort(mSortedSpacesList, this::CompareLocationItems);
        UpdateRecycler();
    }

    private static int fittingAlign(String a, String b) {
        if (a.length() == 0 || b.length() == 0) return 0;

        char[] arrA = a.toLowerCase().toCharArray();
        char[] arrB = b.toLowerCase().toCharArray();

        int[][] scores = new int[a.length()][b.length()];
        scores[0][0] = 0;
        int maxScore = 0;

        for (int init = 0; init < Math.max(a.length(), b.length()); init++) {
            if (init < a.length()) scores[init][0] = arrA[init] == arrB[0] ? 1 : 0;
            if (init < b.length()) scores[0][init] = arrB[init] == arrA[0] ? 1 : 0;
        }

        for (int i = 1; i < a.length(); i++) {
            for (int j = 1; j < b.length(); j++) {
                int match = scores[i - 1][j - 1] + (arrA[i] == arrB[j] ? 1 : -1);
                int delOne = Math.max(scores[i - 1][j], scores[i][j - 1]) - 1;
                scores[i][j] = Math.max(0, Math.max(match, delOne));
                maxScore = Math.max(maxScore, scores[i][j]);
            }
        }

        return maxScore;
    }
}