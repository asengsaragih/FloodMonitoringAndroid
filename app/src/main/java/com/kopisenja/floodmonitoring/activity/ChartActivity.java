package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kopisenja.floodmonitoring.R;

import java.util.ArrayList;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    private LineChart mDataLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mDataLineChart = findViewById(R.id.LineChart_data);

        mShowChart();
    }

    private void mShowChart() {
        Query query;

        query = FirebaseDatabase.getInstance().getReference().child("Recent").child("Device1").limitToFirst(20);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot valueSnapshot : dataSnapshot.getChildren()) {
//                    String time = valueSnapshot.getValue()
//                }
//                String time = dataSnapshot.child(dataSnapshot.getKey()).child("time").getValue(String.class);
//
//                Log.d("TES_KONSOL", time);

                collectDebit((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void collectDebit(Map<String,Object> value) {
        ArrayList<String> debit = new ArrayList<>();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleData = (Map) entry.getValue();

            debit.add((String) singleData.get("debit"));
        }

        Log.d("TES_KONSOL", debit.toString());
    }


}
