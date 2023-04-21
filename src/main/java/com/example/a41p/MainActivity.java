    package com.example.a41p;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

    public class MainActivity extends AppCompatActivity {

        private TextView workoutTimeTextView;
        private EditText workoutTimeEditText;
        private TextView restTimeTextView;
        private EditText restTimeEditText;
        private Button startButton;
        private Button stopButton;
        private ProgressBar progressBar;
        private MediaPlayer mediaPlayer;


        private CountDownTimer workoutTimer;
    private CountDownTimer restTimer;
    private Handler handler;
    private Runnable runnable;

    private int workoutTime;
    private int restTime;
    private boolean isWorkoutPhase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        workoutTimeEditText = findViewById(R.id.workoutTimeEditText);
        restTimeEditText = findViewById(R.id.restTimeEditText);
        workoutTimeTextView = findViewById(R.id.workoutTimeTextView);
        restTimeTextView = findViewById(R.id.restTimeTextView);
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        // Set click listeners for start and stop buttons
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(v);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer(v);
            }
        });
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
                handler.postDelayed(this, 1000);
            }
        };
    }
    public void startTimer(View view) {
        workoutTime = Integer.parseInt(workoutTimeEditText.getText().toString());
        restTime = Integer.parseInt(restTimeEditText.getText().toString());

        workoutTimer = new CountDownTimer(workoutTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                workoutTimeTextView.setText("Workout Time: " + formatTime(millisUntilFinished));
                progressBar.setProgress((int) ((millisUntilFinished / (float) (workoutTime * 1000)) * 100));
            }

            @Override
            public void onFinish() {
                playSound();
                restTimer.start();
                isWorkoutPhase = false;

                workoutTimer.cancel(); // cancel the workout timer
            }
        };

        restTimer = new CountDownTimer(restTime * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                restTimeTextView.setText("Rest Time: " + formatTime(millisUntilFinished));
                progressBar.setProgress((int) ((millisUntilFinished / (float) (restTime * 1000)) * 100));

                progressBar.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                workoutTimer.start();
                isWorkoutPhase = true;
                playSound();
                restTimer.cancel(); // cancel the rest timer
            }
        };


        workoutTimer.start();
        handler.postDelayed(runnable, 1000);

        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }
        public void stopTimer(View view) {
            workoutTimer.cancel();
            if (restTimer != null) {
                restTimer.cancel();
            }
            handler.removeCallbacks(runnable);
            workoutTimeTextView.setText("Workout Time: 00:00");
            restTimeTextView.setText("Rest Time: 00:00");
            progressBar.setProgress(0);

            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }



        private void updateUI() {
        if (isWorkoutPhase) {
            workoutTimeTextView.setTextColor(Color.RED);
            restTimeTextView.setTextColor(Color.BLACK);
        } else {
            workoutTimeTextView.setTextColor(Color.BLACK);
            restTimeTextView.setTextColor(Color.RED);
        }
    }

        private void playSound() {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound);
            try {
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    private String formatTime(long millis) {
        int minutes = (int) (millis / (1000 * 60));
        int seconds = (int) ((millis / 1000) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
    }