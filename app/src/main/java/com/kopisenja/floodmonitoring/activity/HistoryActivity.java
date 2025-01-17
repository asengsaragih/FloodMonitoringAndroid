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
import com.google.firebase.database.Query;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.adapter.HistoryAdapter;
import com.kopisenja.floodmonitoring.base.FloodData;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mHistoryRecycleView;
    private View mEmptyView;
    private HistoryAdapter mAdapter;
    private ArrayList<FloodData> mData;
    private ArrayList<String> mDataId;
    private DatabaseReference mDatabase;
    private ImageView mBackButton;
    private TextView mTitleTextView;
    private HashMap<String, String> mLocationHashMap;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mData.add(dataSnapshot.getValue(FloodData.class));
            mDataId.add(dataSnapshot.getKey());
            mAdapter.updateEmptyView();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mData.set(pos, dataSnapshot.getValue(FloodData.class));
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
//            tes komit
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        mHistoryRecycleView = findViewById(R.id.recycleView_history);
        mEmptyView = findViewById(R.id.emptyView_history);
        mBackButton = findViewById(R.id.imageView_back);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showHistory(savedInstanceState);
        showTitleBar(savedInstanceState);
    }

    private void showTitleBar(Bundle savedInstanceState) {
        mTitleTextView = findViewById(R.id.textView_title_history);

        String text = "<font color='#828282'>Status Terkini</font><br><font color='#93CFF2'>" + nameLocation(savedInstanceState) + "</font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTitleTextView.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            mTitleTextView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }

    private String idLocation(Bundle savedInstanceState) {
        String id_location;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                return id_location = null;

            } else {
                return id_location = extras.getString("CODE_LOCATION");
            }
        } else {
            return id_location = (String) savedInstanceState.getSerializable("CODE_LOCATION");
        }
    }

    private String nameLocation(Bundle savedInstanceState) {
        String name_location;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                return name_location = null;

            } else {
                return name_location = extras.getString("NAME_LOCATION");
            }
        } else {
            return name_location = (String) savedInstanceState.getSerializable("NAME_LOCATION");
        }
    }

    private void showHistory(Bundle savedInstanceState) {
        String key = idLocation(savedInstanceState);

        mData = new ArrayList<>();
        mDataId = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Marker").child(key).child("recent");

        Query queryLimit = mDatabase.orderByChild("status").equalTo(1).limitToLast(20);

        queryLimit.addChildEventListener(childEventListener);

        mHistoryRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mHistoryRecycleView.setLayoutManager(linearLayoutManager);

//        DividerItemDecoration divider = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
//        mHistoryRecycleView.addItemDecoration(divider);

        mAdapter = new HistoryAdapter(this, mData, mDataId, mEmptyView, new HistoryAdapter.ClickHandler() {
            @Override
            public void onItemClick(int position) {
                //ketika item di klik
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });

        mHistoryRecycleView.setAdapter(mAdapter);
    }


}
