package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.base.Flood;
import com.kopisenja.floodmonitoring.base.FloodData;
import com.kopisenja.floodmonitoring.base.OtherMarker;

import java.util.Calendar;

import static com.kopisenja.floodmonitoring.base.FunctionClass.ToastMessage;
import static com.kopisenja.floodmonitoring.base.FunctionClass.getCurrentDate;
import static com.kopisenja.floodmonitoring.base.FunctionClass.getCurrentTime;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.textviewTengah);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importJsonFirebase(textView);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    private void intentIndex() {
        Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
        startActivity(intent);
    }

    private void importJsonFirebase(TextView clickableTextview) {
        clickableTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mDatabaseReferences = FirebaseDatabase.getInstance().getReference("Recent").child("Device1");
//                String keyID = mDatabaseReferences.push().getKey();
//                mDatabaseReferences.child(keyID).setValue(new Flood(
//                        getCurrentDate(),
//                        getCurrentTime(),
//                        "Bojongsoang",
//                        "12 L/m",
//                        "1.3 cm",
//                        1,
//                        1
//                )).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        ToastMessage(MainActivity.this, "Berhasil", 1);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        ToastMessage(MainActivity.this, "Gagal", 1);
//                    }
//                });
                mDatabaseReferences = FirebaseDatabase.getInstance().getReference("Marker").child("-M3hNU9HV4ZkuWsSgxcc").child("recent");
                String keyID = mDatabaseReferences.push().getKey();
                mDatabaseReferences.child(keyID).setValue(new FloodData(
                        getCurrentDate(),
                        getCurrentTime(),
                        "31 L/m",
                        "1.5 cm",
                        1,
                        1,
                        "-" + System.currentTimeMillis()
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ToastMessage(MainActivity.this, "Berhasil", 1);
                    }
                });
            }
        });

        clickableTextview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDatabaseReferences = FirebaseDatabase.getInstance().getReference("Recent").child("Device2");
                String keyID = mDatabaseReferences.push().getKey();
                mDatabaseReferences.child(keyID).setValue(new Flood(
                        getCurrentDate(),
                        getCurrentTime(),
                        "Radio",
                        "19 L/m",
                        "1.2 cm",
                        1,
                        1
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ToastMessage(MainActivity.this, "Berhasil", 1);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastMessage(MainActivity.this, "Gagal", 1);
                    }
                });
                return false;
            }
        });
    }
}
