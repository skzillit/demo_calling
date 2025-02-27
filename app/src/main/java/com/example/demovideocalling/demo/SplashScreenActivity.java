package com.example.demovideocalling.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demovideocalling.R;

public class SplashScreenActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    findViewById(R.id.mediasoup)
        .postDelayed(() -> startActivity(new Intent(this, RoomActivity.class)), 1000);
  }
}
