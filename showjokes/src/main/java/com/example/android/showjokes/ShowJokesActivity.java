package com.example.android.showjokes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowJokesActivity extends AppCompatActivity {

    TextView jokesTextView;
    String joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_jokes);

        jokesTextView = findViewById(R.id.tv_joke);

        if (!getIntent().hasExtra("joke")){
            joke = getString(R.string.no_jokes);
        } else {
            joke = getIntent().getStringExtra("joke");
        }

        jokesTextView.setText(joke);
    }

    public static void showJokes(Context context, String joke){
        Intent intent = new Intent(context, ShowJokesActivity.class);
        intent.putExtra("joke", joke);
        context.startActivity(intent);
    }
}
