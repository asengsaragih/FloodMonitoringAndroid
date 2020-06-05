package com.kopisenja.floodmonitoring.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.base.PrefManagerIntro;

public class IntroActivity extends AppCompatActivity {

    private ScrollView mIntroOneScrollView;
    private ScrollView mIntroTwoScrollView;
    private Button mSkipButton;
    private Button mNextButton;
    private PrefManagerIntro mSessionIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        mSessionIntro = new PrefManagerIntro(this);
        if (mSessionIntro.intro()) {
            startActivity(new Intent(this, IndexActivity.class));
        }

        mIntroOneScrollView = findViewById(R.id.scrollView_intro_one);
        mIntroTwoScrollView = findViewById(R.id.scrollView_intro_two);

        mSkipButton = findViewById(R.id.button_intro_left);
        mNextButton = findViewById(R.id.button_intro_right);

        mButtonFunction();
    }

    private void mButtonFunction() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIntroOneScrollView.getVisibility() == View.VISIBLE) {
                    mIntroOneScrollView.setVisibility(View.GONE);
                    mIntroTwoScrollView.setVisibility(View.VISIBLE);
                } else {
                    mSessionIntro.setIntro(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("FLOOD_MONITORING");
                    Intent i = new Intent(getApplicationContext(), IndexActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSessionIntro.setIntro(true);
                Intent i = new Intent(getApplicationContext(), IndexActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
