package com.flyjingfish.formattextviewdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flyjingfish.formattextview.FormatTextView;

import java.util.Arrays;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FormatTextView formatTextView = findViewById(R.id.text1);
        formatTextView.setFormatText(R.string.test_text,new int[]{R.string.we,R.string.you});
        formatTextView.setFormatText(R.string.test_text,new String[]{"wo","ni"});
    }
}
