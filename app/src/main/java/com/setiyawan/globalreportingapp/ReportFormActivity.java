package com.setiyawan.globalreportingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReportFormActivity extends AppCompatActivity {
    // declare keys
    private static final String NOMOR = "nomor";
    private static final String DAY = "hari";
    private static final String DATE = "tanggal";
    private static final String ICAO = "icao";
    private static final String CALC1 = "calc1";
    private static final String CALC2 = "calc2";
    private static final String CALC3 = "calc3";
    private static final String CALC4 = "calc4";
    private static final String CALC5 = "calc5";
    private static final String CONDITION = "kondisi";
    private static final String AUTHOR = "petugas";

    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    // declare variables
    private EditText reportNumber, reportDay, reportDate, reportIcao, reportCalc1, reportCalc2, reportCalc3, reportCalc4, reportCalc5;
    private String condition, author;
    private AppCompatButton reportRain, reportAfterRain, reportSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);

        // hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // init variables
        reportNumber = findViewById(R.id.report_number);
        reportDay = findViewById(R.id.report_day);
        reportDate = findViewById(R.id.report_date);
        reportIcao = findViewById(R.id.report_icao);
        reportCalc1 = findViewById(R.id.report_calc1);
        reportCalc2 = findViewById(R.id.report_calc2);
        reportCalc3 = findViewById(R.id.report_calc3);
        reportCalc4 = findViewById(R.id.report_calc4);
        reportCalc5 = findViewById(R.id.report_calc5);
        reportRain = findViewById(R.id.report_rain);
        reportAfterRain = findViewById(R.id.report_after_rain);
        reportSave = findViewById(R.id.report_save);

        db = FirebaseFirestore.getInstance();

        // get extra from intent
        Bundle bundle = getIntent().getExtras();
        author = bundle.getString(EXTRA_EMAIL);

        // init condition
        reportRain.setOnClickListener(v -> {
            condition = "Hujan";
        });

        reportAfterRain.setOnClickListener(v -> {
            condition = "Setelah Hujan";
        });

        // save data
        reportSave.setOnClickListener(v -> {
            if (reportNumber.length() > 0 && reportDay.length() > 0 && reportDate.length() > 0 &&
                    reportIcao.length() > 0 && reportCalc1.length() > 0 && reportCalc2.length() > 0 &&
                    reportCalc3.length() > 0 && reportCalc3.length() > 0 && reportCalc4.length() > 0 &&
                    reportCalc5.length() > 0 && condition.length() > 0) {
                saveData(reportNumber.getText().toString(), reportDay.getText().toString(), reportDate.getText().toString(),
                        reportIcao.getText().toString(), reportCalc1.getText().toString(), reportCalc2.getText().toString(),
                        reportCalc3.getText().toString(), reportCalc4.getText().toString(), reportCalc5.getText().toString(),
                        condition, author);
            } else {
                Toast.makeText(getApplicationContext(), "Mohon isi semua data yang diminta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData(String number, String day, String date, String icao, String calc1, String calc2, String calc3, String calc4, String calc5, String condition, String author) {
        Long id = System.currentTimeMillis();

        Map<String, Object> report = new HashMap<>();
        report.put("id", String.valueOf(id));
        report.put(NOMOR, number);
        report.put(DAY, day);
        report.put(DATE, date);
        report.put(ICAO, icao);
        report.put(CALC1, calc1);
        report.put(CALC2, calc2);
        report.put(CALC3, calc3);
        report.put(CALC4, calc4);
        report.put(CALC5, calc5);
        report.put(CONDITION, condition);
        report.put(AUTHOR, author);

        db.collection("reports")
                .add(report)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Report berhasil disimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Report gagal disimpan", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}