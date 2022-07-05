package com.setiyawan.globalreportingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class NavigatorActivity extends AppCompatActivity {
    // declare keys
    private static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static String EXTRA_ROLE = "EXTRA_ROLE";
    private static String EXTRA_ICAO = "EXTRA_ICAO";

    // declare variables
    private ImageView ivLogoutNav;
    private AppCompatButton btnGuideNav, btnListReportNav;
    private FirebaseAuth mAuth;
    private String userRole, userEmail, userIcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        //hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // init variables
        ivLogoutNav = findViewById(R.id.iv_logout_nav);
        btnGuideNav = findViewById(R.id.btn_guide_nav);
        btnListReportNav = findViewById(R.id.btn_list_report_nav);

        mAuth = FirebaseAuth.getInstance();

        // get extras from intent
        Bundle bundle = getIntent().getExtras();
        userEmail = bundle.getString(EXTRA_EMAIL);
        userRole = bundle.getString(EXTRA_ROLE);
        userIcao = bundle.getString(EXTRA_ICAO);

        // sign out logic
        ivLogoutNav.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        // navigate to list report
        btnListReportNav.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReportListActivity.class);
            intent.putExtra(EXTRA_EMAIL, userEmail);
            intent.putExtra(EXTRA_ICAO, userIcao);
            intent.putExtra(EXTRA_ROLE, userRole);
            startActivity(intent);
        });

        // navigate to guide
        btnGuideNav.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), GuideActivity.class));
        });
    }
}