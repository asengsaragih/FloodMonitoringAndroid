package com.kopisenja.floodmonitoring.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kopisenja.floodmonitoring.R;

public class AboutActivity extends AppCompatActivity {

    String emailAldi = "aldiwahyu.saragih@gmail.com";
    String emailHanna = "athiyyatulfarhanah14@gmail.com";

    String instagramAldi = "aldi_saragih";
    String instagramHanna = "afarhannah";

    String githubAldi = "asengsaragih";
    String dribbbleHanna = "afarhanah";

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
                intentInstagram(instagramAldi);
            }
        });

        mInstagramHannaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentInstagram(instagramHanna);
            }
        });

        mGmailAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentGmail(emailAldi);
            }
        });

        mGmailHannaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentGmail(emailHanna);
            }
        });

        mGithubAldiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://github.com/" + githubAldi)));
            }
        });

        mDribbbleHannaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://dribbble.com/" + dribbbleHanna);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("mathieumaree.rippple");

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://dribbble.com/" + dribbbleHanna)));
                }
            }
        });
    }

    private void intentGmail(String username) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: " + username));
        startActivity(Intent.createChooser(intent, "Send Feedback"));
    }

    private void intentInstagram(String username) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + username);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.instagram.com/" + username)));
        }
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
