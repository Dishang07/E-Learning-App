package com.example.projectt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private ProgressBar progressBar;

    private ArrayList<HashMap<String, String>> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private ArrayList<String> userAnswers = new ArrayList<>();

    // Firebase Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load quiz questions
        loadQuizQuestions();

        // Set OnClickListener for the Next button
        nextButton.setOnClickListener(view -> checkAnswer());
    }

    private void loadQuizQuestions() {
        progressBar.setVisibility(View.VISIBLE);

        // Fetch all documents from the 'quizzes' collection
        db.collection("quizzes")
                .get() // Get all documents
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Iterate through each document
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            HashMap<String, String> questionData = new HashMap<>();
                            questionData.put("question", document.getString("question"));
                            questionData.put("option1", document.getString("option1"));
                            questionData.put("option2", document.getString("option2"));
                            questionData.put("option3", document.getString("option3"));
                            questionData.put("option4", document.getString("option4"));
                            questionData.put("correctAnswer", document.getString("Correct Answer"));  // Correct answer text
                            questionList.add(questionData);
                        }
                        progressBar.setVisibility(View.GONE);
                        displayQuestion();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(QuizActivity.this, "No questions found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(QuizActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            HashMap<String, String> currentQuestion = questionList.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.get("question"));
            option1.setText(currentQuestion.get("option1"));
            option2.setText(currentQuestion.get("option2"));
            option3.setText(currentQuestion.get("option3"));
            option4.setText(currentQuestion.get("option4"));
        } else {
            // Show the results when all questions are answered
            showResults();
        }
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);

        if (selectedRadioButton == null) {
            Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedAnswer = selectedRadioButton.getText().toString();
        HashMap<String, String> currentQuestion = questionList.get(currentQuestionIndex);
        String correctAnswer = currentQuestion.get("correctAnswer");

        // Compare the selected answer with the correct answer (which is now a string)
        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect! Correct answer is: " + correctAnswer, Toast.LENGTH_LONG).show();
        }

        userAnswers.add(selectedAnswer);  // Store the user's answer

        currentQuestionIndex++;
        optionsGroup.clearCheck();
        displayQuestion();
    }

    private void showResults() {
        // Show final score
        String resultMessage = "Quiz finished! Your score: " + score + " out of " + questionList.size();
        Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
        resultIntent.putExtra("score", score);
        resultIntent.putExtra("totalQuestions", questionList.size());
        resultIntent.putExtra("userAnswers", userAnswers);
        resultIntent.putExtra("questionList", questionList); // Pass the questions data
        startActivity(resultIntent);
        finish(); // Close the current quiz activity
    }
}
