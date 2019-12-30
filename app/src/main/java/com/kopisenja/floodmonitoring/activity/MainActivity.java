package com.kopisenja.floodmonitoring.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.Calendar;

import static com.kopisenja.floodmonitoring.base.FunctionClass.ToastMessage;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textviewTengah);
        importJsonFirebase(textView);
    }

    private void importJsonFirebase(TextView clickableTextview) {
        clickableTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReferences = FirebaseDatabase.getInstance().getReference("Recent");
                String keyID = mDatabaseReferences.push().getKey();
                mDatabaseReferences.child(keyID).setValue(new Flood(
                        Long.toString(System.currentTimeMillis()),
                        "-6.975464, 107.633257",
                        "20.12",
                        "300",
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
            }
        });
    }
}
