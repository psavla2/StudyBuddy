package edu.illinois.cs465.studybuddy;

import androidx.annotation.NonNull;

import java.util.Set;

public class StudySpace {
    int id;
    String maps_id;
    String name;
    String short_name;
    String description;
    Set<Integer> tags;
    String website;
    String image_id;

    @NonNull
    @Override
    public String toString() {
        return "StudySpace{" +
                "id=" + id +
                ", maps_id='" + maps_id + '\'' +
                ", name='" + name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                ", website='" + website + '\'' +
                '}';
    }
}
