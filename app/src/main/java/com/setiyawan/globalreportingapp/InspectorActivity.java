package com.setiyawan.globalreportingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class InspectorActivity extends AppCompatActivity {
    // declare constant
    private static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static String EXTRA_ROLE = "EXTRA_ROLE";
    private static String EXTRA_ICAO = "EXTRA_ICAO";

    // declare variables
    private ImageView ivLogout;
    private AppCompatButton btnReport, btnGuide, btnListReport;
    private FirebaseAuth mAuth;
    private String userRole, userEmail, userIcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector);

        // hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // init variables
        ivLogout = findViewById(R.id.iv_logout_nav);
        btnReport = findViewById(R.id.btn_report);
        btnGuide = findViewById(R.id.btn_guide);
        btnListReport = findViewById(R.id.btn_list_report);

        mAuth = FirebaseAuth.getInstance();

        // get extras from intent
        Bundle bundle = getIntent().getExtras();
        userEmail = bundle.getString(EXTRA_EMAIL);
        userRole = bundle.getString(EXTRA_ROLE);
        userIcao = bundle.getString(EXTRA_ICAO);

        // sign out logic
        ivLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        // navigate to list report
        btnListReport.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportListActivity.class);
            intent.putExtra(EXTRA_EMAIL, userEmail);
            intent.putExtra(EXTRA_ICAO, userIcao);
            intent.putExtra(EXTRA_ROLE, userRole);
            startActivity(intent);
        });

        // navigate to guide
        btnGuide.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GuideActivity.class));
        });

        // navigate to report
        btnReport.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportFormActivity.class);
            intent.putExtra(EXTRA_EMAIL, userEmail);
            startActivity(intent);
        });
    }
}