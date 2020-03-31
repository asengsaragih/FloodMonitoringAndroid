package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.kopisenja.floodmonitoring.adapter.OtherAdapter;
import com.kopisenja.floodmonitoring.base.OtherMarker;

import java.util.ArrayList;

public class OtherMarkerActivity extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView mTitleTextView;

    private RecyclerView mOtherRecycleView;
    private View mEmptyView;

    private OtherAdapter mAdapter;
    private ArrayList<OtherMarker> mData;
    private ArrayList<String> mDataId;
    private DatabaseReference mDatabase;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mData.add(dataSnapshot.getValue(OtherMarker.class));
            mDataId.add(dataSnapshot.getKey());
            mAdapter.updateEmptyView();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mData.set(pos, dataSnapshot.getValue(OtherMarker.class));
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
        setContentView(R.layout.activity_other_marker);
        getSupportActionBar().hide();

        mBackButton = findViewById(R.id.imageView_back_other);
        mEmptyView = findViewById(R.id.emptyView_other);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), IndexActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
//                finish();
            }
        });

        showTitleBar();
        showRecycle();
    }

    private void showRecycle() {

        mData = new ArrayList<>();
        mDataId = new ArrayList<>();

        mOtherRecycleView = findViewById(R.id.recycleView_other);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Marker");
        mDatabase.addChildEventListener(childEventListener);

        mOtherRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mOtherRecycleView.setLayoutManager(linearLayoutManager);

        mAdapter = new OtherAdapter(this, mData, mDataId, mEmptyView, new OtherAdapter.ClickHandler() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        mOtherRecycleView.setAdapter(mAdapter);
    }

    private void showTitleBar() {
        mTitleTextView = findViewById(R.id.textView_title_other);

        String text = "<font color='#828282'>Daftar Lokasi</font><br><font color='#93CFF2'>Perangkat</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTitleTextView.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            mTitleTextView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }
}
