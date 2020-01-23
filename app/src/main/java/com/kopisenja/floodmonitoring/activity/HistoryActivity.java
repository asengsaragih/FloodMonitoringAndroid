package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.adapter.HistoryAdapter;
import com.kopisenja.floodmonitoring.base.Flood;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mHistoryRecycleView;
    private View mEmptyView;
    private HistoryAdapter mAdapter;
    private ArrayList<Flood> mData;
    private ArrayList<String> mDataId;
    private DatabaseReference mDatabase;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mData.add(dataSnapshot.getValue(Flood.class));
            mDataId.add(dataSnapshot.getKey());
            mAdapter.updateEmptyView();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            int pos = mDataId.indexOf(dataSnapshot.getKey());
            mData.set(pos, dataSnapshot.getValue(Flood.class));
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
        setContentView(R.layout.activity_history);

        mHistoryRecycleView = findViewById(R.id.recycleView_history);
        mEmptyView = findViewById(R.id.emptyView_history);

        showHistory();
    }

    private void showHistory() {
        mData = new ArrayList<>();
        mDataId = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("Recent").child("Device1");
        mDatabase.addChildEventListener(childEventListener);

        mHistoryRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mHistoryRecycleView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        mHistoryRecycleView.addItemDecoration(divider);

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
