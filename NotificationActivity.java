package com.example.projectt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projectt.model.Notification;
import com.example.projectt.adapters.NotificationAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification); // Make sure this layout has a ListView

        // Get the ListView to display the notifications
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView listView = findViewById(R.id.notificationListView);

        // Initialize Firestore and fetch notifications
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");

        // Fetch notifications from Firestore
        notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Notification> notificationList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String subject = document.getString("subject");
                    String description = document.getString("description");
                    Date date = document.getDate("date"); // Assuming the date field is a Firestore Timestamp

                    // Create Notification object and add to list
                    Notification notification = new Notification(subject, description, date);
                    notificationList.add(notification);
                }

                // Create adapter and set it to ListView
                NotificationAdapter adapter = new NotificationAdapter(notificationList, this);
                listView.setAdapter(adapter);
            } else {
                // Handle errors (if any)
                Log.e("Firestore", "Error getting notifications: ", task.getException());
            }
        });
    }
}
