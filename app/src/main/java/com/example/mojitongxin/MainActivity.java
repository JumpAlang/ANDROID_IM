package com.example.mojitongxin;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.example.mojitongxin.ui.RotateView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RotateView rotateView = findViewById(R.id.RotateView);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test1);

        rotateView.setSource(bitmap);
    }
}
