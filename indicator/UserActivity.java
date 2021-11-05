package com.example.indicator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;



public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar indicator = (ProgressBar) findViewById(R.id.progress_bar_indicator);
        TextView percentage = (TextView) findViewById(R.id.percentage_indicator);
        TextView ratio = (TextView) findViewById(R.id.finished_all_ratio_indicator);

        /*
        Get numbers from User Class: finishedThisWeek, habitDataList.size(). Get and set Ratio!
         */
        percentage.setText("66%");
        indicator.setProgress(66, true);
        ratio.setText("6/9");
    }


}