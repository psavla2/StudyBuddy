package edu.illinois.cs465.studybuddy;

import androidx.annotation.NonNull;

import java.util.Set;

public class StudySpace {
    int id;
    String name;
    String description;
    double latitude;
    double longitude;
    Set<Integer> tags;

    @NonNull
    @Override
    public String toString() {
        return "StudySpace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", tags=" + tags +
                '}';
    }
}
