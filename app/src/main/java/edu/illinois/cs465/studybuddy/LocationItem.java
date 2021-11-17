package edu.illinois.cs465.studybuddy;

import java.util.Locale;


public class LocationItem {
    private int imageDrawable;
    private String name;
    private String description;
    private int id;

    public LocationItem(String title, String subtitle, int id) {
        imageDrawable = R.drawable.elcap;
        name = title;
        description = subtitle;
        this.id = id;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }
}
