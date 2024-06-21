package com.atomyx.ronniecolemeter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText etWeight;
    private GridLayout container;
    private Button btnSubmit;
    private Bitmap bitmapRC;
    private final int RC = 135;
    private ArrayList<ImageView> ivList = new ArrayList<>();
    private int displayWidth;
    private int bitmapRCWidth;
    private int bitmapRCHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayWidth = getResources().getDisplayMetrics().widthPixels;
        etWeight = findViewById(R.id.etWeight);
        btnSubmit = findViewById(R.id.btnSubmit);
        container = findViewById(R.id.container);

        bitmapRC = ((BitmapDrawable) Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.rc, null))).getBitmap();
        bitmapRCWidth = displayWidth / 9;
        bitmapRCHeight = bitmapRCWidth * 1044 / 538;
        bitmapRC = Bitmap.createScaledBitmap(bitmapRC, bitmapRCWidth, bitmapRCHeight, true);

        btnSubmit.setOnClickListener(e -> btnSubmitClicked());

    }

    private void btnSubmitClicked() {
        ivList.clear();
        container.removeAllViews();

        String weightStr = etWeight.getText().toString();
        double weight = 0;
        if (weightStr.matches("\\d+([.,]\\d*)*")) {
            weight = Double.parseDouble(weightStr);
        }
        if (weight == 0) {
            return;
        }

        int fullRcNumber = (int) weight / RC;
        for (int i = 0; i < fullRcNumber; i++) {
            ImageView iv = new ImageView(MainActivity.this);
            iv.setImageBitmap(bitmapRC);
            ivList.add(iv);
        }

        double partialRcSize = weight % RC;
        if (partialRcSize != 0) {
            double rcHeightUnit = (double) bitmapRCHeight / RC;
            int bitmapRCPartialHeight = (int) (rcHeightUnit * partialRcSize);
            Bitmap bitmapPartialRC = Bitmap.createBitmap(bitmapRC, 0, 0, bitmapRCWidth, bitmapRCPartialHeight);
            ImageView iv = new ImageView(MainActivity.this);
            iv.setImageBitmap(bitmapPartialRC);
            ivList.add(iv);
        }

        int row = 0;
        int col = 0;
        for (ImageView iv : ivList) {
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.rowSpec = GridLayout.spec(row);
            lp.columnSpec = GridLayout.spec(col);
            iv.setLayoutParams(lp);
            if (col < 2) {
                col++;
            } else {
                row++;
                col = 0;
            }
            container.addView(iv);
        }

    }

}
