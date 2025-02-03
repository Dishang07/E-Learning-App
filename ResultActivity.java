package com.example.projectt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView detailedResultsTextView;
    private Button finishButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.resultTextView);
        detailedResultsTextView = findViewById(R.id.detailedResultsTextView);

        // Retrieve data passed from QuizActivity
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        ArrayList<String> userAnswers = getIntent().getStringArrayListExtra("userAnswers");
        ArrayList<HashMap<String, String>> questionList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("questionList");

        // Display final score
        String resultMessage = "Your Score: " + score + " out of " + totalQuestions;
        resultTextView.setText(resultMessage);

        // Display detailed results
        StringBuilder detailedResults = new StringBuilder();
        for (int i = 0; i < questionList.size(); i++) {
            HashMap<String, String> question = questionList.get(i);
            String userAnswer = userAnswers.get(i);
            String correctAnswer = question.get("correctAnswer");
            detailedResults.append("Q").append(i + 1).append(": ").append(question.get("question"))
                    .append("\nYour Answer: ").append(userAnswer)
                    .append("\nCorrect Answer: ").append(correctAnswer)
                    .append("\n\n");
        }

        // Set the detailed results in the TextView
        detailedResultsTextView.setText(detailedResults.toString());

    }
}
