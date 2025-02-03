package com.example.projectt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnCourses, btnNotifications,btnquiz;
    private Bundle onCreate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(onCreate);
        setContentView(R.layout.activity_user_profile);

        tvUserName = findViewById(R.id.tv_user_name);
        btnCourses = findViewById(R.id.btn_courses);
        btnNotifications = findViewById(R.id.btn_notifications);
        btnquiz=findViewById(R.id.btnq);
        // Replace btn_quizzes

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userName = user.getDisplayName(); // Or use user.getEmail()
            tvUserName.setText("Welcome,Choose One" );
        }

        btnCourses.setOnClickListener(view -> {
            Intent coursesIntent = new Intent(this, CourseLibraryActivity.class);
            startActivity(coursesIntent);
        });

        btnNotifications.setOnClickListener(view -> {
            Intent notificationIntent = new Intent(this, NotificationActivity.class);  // Assuming NotificationActivity handles notifications
            startActivity(notificationIntent);
        });
        btnquiz.setOnClickListener(view -> {
            Intent quizIntent = new Intent(this, QuizActivity.class);
            startActivity(quizIntent);
        });
    }
}