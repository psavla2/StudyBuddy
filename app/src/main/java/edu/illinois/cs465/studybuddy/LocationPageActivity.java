package edu.illinois.cs465.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;

public class LocationPageActivity extends AppCompatActivity{

    private TextView signal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        /**/

        //signal = findViewById(R.id.signal);

        ChipGroup filters = findViewById(R.id.filters_chip_group);
        Tag[] tags = JsonReader.getTags(this);

        for (Tag t : tags) {
            Chip chip = new Chip(new ContextThemeWrapper(this, R.style.Widget_Material3_Chip_Filter));
            chip.setCheckable(true);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            chip.setLayoutParams(layoutParams);
            chip.setId(t.id);
            chip.setText(t.tag);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                CharSequence newText;
                if (isChecked) {
                    newText = String.format(Locale.US, "Checked tag with id: %d", buttonView.getId());
                } else {
                    newText = String.format(Locale.US, "Unchecked tag with id: %d", buttonView.getId());
                }
                //signal.setText(newText);
            });
            filters.addView(chip);
        }
    }

}


