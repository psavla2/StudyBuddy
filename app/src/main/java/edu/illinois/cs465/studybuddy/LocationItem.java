package edu.illinois.cs465.studybuddy;

import java.util.Locale;


public class LocationItem {
    private int imageDrawable;
    private StudySpace space;

    public LocationItem(StudySpace space) {
        imageDrawable = R.drawable.elcap;
        this.space = space;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public String getName() {
        return space.name;
    }

    public String getDescription() {
        return space.description;
    }

    public int getId() {
        return space.id;
    }

    public StudySpace getStudySpace() {
        return space;
    }
}
