package com.example.volumeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText enterVolume;
    Button setVolume;
    TextView textView;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterVolume = findViewById(R.id.enterVolume);
        setVolume = findViewById(R.id.setVolume);
        textView = findViewById(R.id.textView);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setVolume.setEnabled(false);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        textView.setText("Current Volume: " + String.valueOf(currentVolume));
        enterVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    setVolume.setEnabled(false);
                } else {
                    setVolume.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setVolume.setOnClickListener(v -> {
            String volumeString = enterVolume.getText().toString().trim();

            // Check if the input is valid
            if (!TextUtils.isDigitsOnly(volumeString) || Integer.parseInt(volumeString) <= 0 || Integer.parseInt(volumeString) > 100 ) {
                // Show an error dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Please enter a number between 1 and 100")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                int volume = Integer.parseInt(volumeString);
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int newVolume = (int) ((maxVolume * volume) / 100.0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
                textView.setText("Current Volume: " + String.valueOf(newVolume));
            }
        });
    }
}
