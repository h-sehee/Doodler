package com.example.doodler;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import yuku.ambilwarna.AmbilWarnaDialog;


public class MainActivity extends AppCompatActivity {

    private DoodleView doodleView;
    private SeekBar strokeSeekBar;
    private SeekBar alphaSeekBar;
    private CustomImageView colorCircle;
    private CustomImageView strokeCircle;
    private CustomImageView opacityCircle;
    private boolean isEraser = false;
    private int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        doodleView = findViewById(R.id.doodleView);
        strokeSeekBar = findViewById(R.id.strokeSeekBar);
        alphaSeekBar = findViewById(R.id.alphaSeekBar);
        colorCircle = findViewById(R.id.colorCircle);
        strokeCircle = findViewById(R.id.strokeCircle);
        opacityCircle = findViewById(R.id.opacityCircle);

        colorCircle.updateCircle(50, 255, doodleView.getPaintColor());
        strokeCircle.updateCircle(doodleView.getPaintStrokeWidth(), 255, Color.BLACK);
        opacityCircle.updateCircle(50, doodleView.getPaintAlpha(), Color.BLACK);

        colorCircle.setOnClickListener(v -> {
            strokeSeekBar.setVisibility(View.GONE);
            alphaSeekBar.setVisibility(View.GONE);
            new AmbilWarnaDialog(this, doodleView.getPaintColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    doodleView.setPaintColor(color);
                    colorCircle.updateCircle(50, 255, color);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
            }).show();
        });

        // 굵기 설정
        strokeCircle.setOnClickListener(v -> {
            strokeSeekBar.setVisibility(View.VISIBLE);
            alphaSeekBar.setVisibility(View.GONE);
            strokeSeekBar.setProgress((int) doodleView.getPaintStrokeWidth());
        });

        // 투명도 설정
        opacityCircle.setOnClickListener(v -> {
            strokeSeekBar.setVisibility(View.GONE);
            alphaSeekBar.setVisibility(View.VISIBLE);
            alphaSeekBar.setProgress(doodleView.getPaintAlpha());
        });

        strokeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    doodleView.setStrokeWidth(progress);
                    strokeCircle.updateCircle(doodleView.getPaintStrokeWidth(), 255, Color.BLACK);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        alphaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                doodleView.setPaintAlpha(progress);
                opacityCircle.updateCircle(50, doodleView.getPaintAlpha(), Color.BLACK);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doodleView.clear();
            }
        });

        Button penButton = findViewById(R.id.penButton);
        penButton.setOnClickListener(v -> {
            isEraser = !isEraser;
            if (isEraser) {
                currentColor = doodleView.getPaintColor();
                doodleView.setPaintColor(Color.WHITE);
            } else {
                doodleView.setPaintColor(currentColor);
            }
            strokeSeekBar.setVisibility(View.GONE);
            alphaSeekBar.setVisibility(View.GONE);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}