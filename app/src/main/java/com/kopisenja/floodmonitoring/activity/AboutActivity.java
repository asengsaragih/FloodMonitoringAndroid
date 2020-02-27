package com.kopisenja.floodmonitoring.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kopisenja.floodmonitoring.R;

public class AboutActivity extends AppCompatActivity {

    private TextView mTitleAppTextView;
    private TextView mTitleAboutDeveloperTextView;
    private ConstraintLayout mDeveloperConstraint;
    private ConstraintLayout mAboutAppConstraint;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private ImageView mBackImageView;
    private ImageView mInstagramAldiImageView;
    private ImageView mInstagramHannaImageView;
    private ImageView mGmailAldiImageView;
    private ImageView mGmailHannaImageView;
    private ImageView mGithubAldiImageView;
    private ImageView mDribbbleHannaImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        mDeveloperConstraint = findViewById(R.id.constraint_tentang_developer);
        mAboutAppConstraint = findViewById(R.id.constraint_tentang_aplikasi);

        mLeftImageView = findViewById(R.id.imageView_button_left);
        mRightImageView = findViewById(R.id.imageView_button_right);
        mBackImageView = findViewById(R.id.imageView_about_back);

        mInstagramAldiImageView = findViewById(R.id.imageView_instagram_aldi);
        mInstagramHannaImageView = findViewById(R.id.imageView_instagram_hanna);
        mGmailAldiImageView = findViewById(R.id.imageView_gmail_aldi);
        mGmailHannaImageView = findViewById(R.id.imageView_gmail_hanna);
        mGithubAldiImageView = findViewById(R.id.imageView_github_aldi);
        mDribbbleHannaImageView = findViewById(R.id.imageView_dribbble_hanna);

        showTitleApp();
        showTitleAboutUs();
        buttonFunction();
    }

    private void buttonFunction() {
        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRightImageView.setVisibility(View.VISIBLE);
                mLeftImageView.setVisibility(View.GONE);

                mAboutAppConstraint.setVisibility(View.VISIBLE);
                mDeveloperConstraint.setVisibility(View.GONE);
            }
        });

        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRightImageView.setVisibility(View.GONE);
                mLeftImageView.setVisibility(View.VISIBLE);

                mAboutAppConstraint.setVisibility(View.GONE);
                mDeveloperConstraint.setVisibility(View.VISIBLE);
            }
        });

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mInstagramAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mInstagramHannaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mGmailAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mGmailHannaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mGithubAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDribbbleHannaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void showTitleApp() {
        mTitleAppTextView = findViewById(R.id.title_about);

        String[] part = getString(R.string.app_name).split(" ");

        String text = part[0] + "<b>" + part[1] + "</b>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTitleAppTextView.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            mTitleAppTextView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }

    private void showTitleAboutUs() {
        mTitleAboutDeveloperTextView = findViewById(R.id.title_developer);

        String[] part = getString(R.string.activity_about).split(" ");

        String text = "<font color='#828282'>" + part[0] + "</font> <br>" + "<font color='#93CFF2'>" + part[1] + "</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTitleAboutDeveloperTextView.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            mTitleAboutDeveloperTextView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }
}
