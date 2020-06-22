package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.adapter.NotificationAdapter;
import com.kopisenja.floodmonitoring.base.Marker;
import com.kopisenja.floodmonitoring.base.Notification;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private ImageView mBackImageView;
    private RecyclerView mNotificationRecycleview;
    private TextView mTitleTextview;
    private View mEmptyView;
    private NotificationAdapter mAdapter;
    private ArrayList<Notification> mData;
    private ArrayList<String> mDataId;
    private DatabaseReference mDatabase;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mData.add(dataSnapshot.getValue(Notification.class));
            mDataId.add(dataSnapshot.getKey());
            mAdapter.updateEmptyView();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mData.set(pos, dataSnapshot.getValue(Notification.class));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mDataId.remove(pos);
            mData.remove(pos);
            mAdapter.updateEmptyView();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initialActivity();
    }

    private void initialActivity() {
        mBackImageView = findViewById(R.id.imageView_notification_back);
        mNotificationRecycleview = findViewById(R.id.recycleView_notification);
        mTitleTextview = findViewById(R.id.textView_title_notification);
        mEmptyView = findViewById(R.id.emptyView_notification);

        getSupportActionBar().hide();

        setTitleText();
        showListData();
    }

    private void setTitleText() {
        String text = "<font color='#828282'>Pengaturan</font><br><font color='#93CFF2'>Notifikasi</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTitleTextview.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            mTitleTextview.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }

    private void showListData() {
        mData = new ArrayList<>();
        mDataId = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Marker");

        mDatabase.addChildEventListener(childEventListener);

        mNotificationRecycleview.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        mNotificationRecycleview.setLayoutManager(linearLayoutManager);

        mAdapter = new NotificationAdapter(this, mData, mDataId, mEmptyView, new NotificationAdapter.ClickHandler() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        mNotificationRecycleview.setAdapter(mAdapter);
    }
}
