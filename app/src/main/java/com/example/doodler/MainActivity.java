package com.example.doodler;

import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

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
    private int previousAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.main).setBackgroundColor(Color.WHITE);

        doodleView = findViewById(R.id.doodleView);
        strokeSeekBar = findViewById(R.id.strokeSeekBar);
        alphaSeekBar = findViewById(R.id.alphaSeekBar);
        colorCircle = findViewById(R.id.colorCircle);
        strokeCircle = findViewById(R.id.strokeCircle);
        TextView strokeText = findViewById(R.id.strokeText);
        opacityCircle = findViewById(R.id.opacityCircle);
        TextView alphaText = findViewById(R.id.alphaText);
        View line = findViewById(R.id.line);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) line.getLayoutParams();

        colorCircle.updateCircle(50, 255, doodleView.getPaintColor());
        strokeCircle.updateCircle(doodleView.getPaintStrokeWidth(), 255, Color.BLACK);
        opacityCircle.updateCircle(50, doodleView.getPaintAlpha(), Color.BLACK);

        colorCircle.setOnClickListener(v -> {
            strokeSeekBar.setVisibility(View.GONE);
            alphaSeekBar.setVisibility(View.GONE);
            params.topMargin = 0;
            line.setLayoutParams(params);
            new AmbilWarnaDialog(this, doodleView.getPaintColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    previousAlpha=doodleView.getPaintAlpha();
                    doodleView.setPaintColor(color);
                    doodleView.setPaintAlpha(previousAlpha);
                    colorCircle.updateCircle(50, 255, color);
                }

                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
            }).show();
        });

        strokeCircle.setOnClickListener(v -> {
            strokeSeekBar.setVisibility(View.VISIBLE);
            alphaSeekBar.setVisibility(View.GONE);
            params.topMargin = (int) (30 * getResources().getDisplayMetrics().density);
            line.setLayoutParams(params);
            strokeSeekBar.setProgress((int) doodleView.getPaintStrokeWidth());
        });

        opacityCircle.setOnClickListener(v -> {
            strokeSeekBar.setVisibility(View.GONE);
            alphaSeekBar.setVisibility(View.VISIBLE);
            params.topMargin = (int) (30 * getResources().getDisplayMetrics().density);
            line.setLayoutParams(params);
            alphaSeekBar.setProgress(doodleView.getPaintAlpha());
        });

        strokeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 0) {
                    doodleView.setStrokeWidth(progress);
                    strokeCircle.updateCircle(doodleView.getPaintStrokeWidth(), 255, Color.BLACK);
                    strokeText.setText((int)doodleView.getPaintStrokeWidth() + "px");
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
                int percent = (int) ((progress / 255.0) * 100);
                alphaText.setText(percent + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ImageButton clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(v -> {
            doodleView.clear();
        });

        ImageButton penButton = findViewById(R.id.penButton);
        penButton.setOnClickListener(v -> {
            isEraser = !isEraser;
            doodleView.setEraserMode(isEraser);
            strokeSeekBar.setVisibility(View.GONE);
            alphaSeekBar.setVisibility(View.GONE);
            params.topMargin = 0;
            line.setLayoutParams(params);
            if (isEraser) {
                penButton.setImageResource(R.drawable.eraser);
            } else {
                penButton.setImageResource(R.drawable.pen);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}