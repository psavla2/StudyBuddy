package edu.illinois.cs465.studybuddy;

import java.util.Locale;


public class LocationItem {
    private StudySpace space;

    public LocationItem(StudySpace space) {
        this.space = space;
    }

    public String getName() {
        return space.name;
    }

    public String getShortName() { return space.short_name; }

    public String getDescription() {
        return space.description;
    }

    public int getId() {
        return space.id;
    }

    public StudySpace getStudySpace() {
        return space;
    }

    public String getImageId() {
        return space.image_id;
    }
}
