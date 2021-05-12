package com.example.statgen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static final String account_text = "com.example.application.example.account_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        openActivity2();

    }

    public void openActivity2() {
        EditText text = findViewById(R.id.editText5);
        String account = text.getText().toString();

        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra(account_text, account);

        startActivity(intent);
    }
}
