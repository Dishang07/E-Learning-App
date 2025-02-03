package com.example.projectt;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectt.adapters.CourseAdapter;
import com.example.projectt.model.Course;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CourseLibraryActivity extends AppCompatActivity {

    private CourseAdapter courseAdapter;
    private List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_library);
        // Initialize ListView and Adapter
        ListView listView = findViewById(R.id.courseListView);
        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseList, this);
        listView.setAdapter(courseAdapter);

        // Fetch courses from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("courses").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String description = document.getString("description");
                            Date duration = document.getDate("duration");
                            DocumentReference linkRef = document.getDocumentReference("link"); // Reference to another collection or URL
                            // Create a new Course object and add it to the list
                            Course course = new Course(title, description, duration, linkRef);
                            courseList.add(course);
                        }

                        // Notify adapter that data has changed
                        courseAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error getting courses: ", task.getException());
                        Toast.makeText(CourseLibraryActivity.this, "Error loading courses", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}