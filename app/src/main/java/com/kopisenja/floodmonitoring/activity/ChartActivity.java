package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.base.Flood;

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
                ArrayList<Entry> debitVals = new ArrayList<Entry>();
                ArrayList<Entry> levelVals = new ArrayList<Entry>();

                if (dataSnapshot.hasChildren()) {

                    int i = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Flood flood = snapshot.getValue(Flood.class);

                        Float debit = Float.parseFloat(flood.getDebit().replace(" L/m", ""));
                        Float level = Float.parseFloat(flood.getLevel().replace(" cm", ""));

                        levelVals.add(new Entry(i++, level));
                        debitVals.add(new Entry(i++, debit));
                    }
                    showChart(debitVals, levelVals);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showChart(ArrayList<Entry> debitVals, ArrayList<Entry> levelVals) {
        LineDataSet lineDataSet1 = new LineDataSet(debitVals, "debit");
        LineDataSet lineDataSet2 = new LineDataSet(levelVals, "level");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData lineData = new LineData(dataSets);

        mDataLineChart.setData(lineData);
        mDataLineChart.invalidate();
    }
}
