package com.setiyawan.globalreportingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class ReportActivity extends AppCompatActivity {
    // declare keys
    private static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static String EXTRA_ICAO = "EXTRA_ICAO";
    private static String EXTRA_ID = "EXTRA_ID";
    private static String EXTRA_HARI = "EXTRA_HARI";
    private static String EXTRA_TANGGAL = "EXTRA_TANGGAL";
    private static String EXTRA_NOMOR = "EXTRA_NOMOR";
    private static String EXTRA_CALC1 = "EXTRA_CALC1";
    private static String EXTRA_CALC2 = "EXTRA_CALC2";
    private static String EXTRA_CALC3 = "EXTRA_CALC3";
    private static String EXTRA_CALC4 = "EXTRA_CALC4";
    private static String EXTRA_CALC5 = "EXTRA_CALC5";
    private static String EXTRA_KONDISI = "EXTRA_KONDISI";

    // declare variables
    private String petugas, icao, id, hari, tanggal, nomor, calc1, calc2, calc3, calc4, calc5, kondisi;
    private TextView tvDetailNomor, tvDetailIcao, tvDetailHari, tvDetailTanggal, tvDetailCalc1, tvDetailCalc2, tvDetailCalc3, tvDetailCalc4, tvDetailCalc5
, tvDetailKondisi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString(EXTRA_ID);
        petugas = bundle.getString(EXTRA_EMAIL);
        icao = bundle.getString(EXTRA_ICAO);
        hari = bundle.getString(EXTRA_HARI);
        tanggal = bundle.getString(EXTRA_TANGGAL);
        nomor = bundle.getString(EXTRA_NOMOR);
        calc1 = bundle.getString(EXTRA_CALC1);
        calc2 = bundle.getString(EXTRA_CALC2);
        calc3 = bundle.getString(EXTRA_CALC3);
        calc4 = bundle.getString(EXTRA_CALC4);
        calc5 = bundle.getString(EXTRA_CALC5);
        kondisi = bundle.getString(EXTRA_KONDISI);

        tvDetailNomor = findViewById(R.id.tv_detail_nomor);
        tvDetailHari = findViewById(R.id.tv_detail_hari);
        tvDetailTanggal = findViewById(R.id.tv_detail_tanggal);
        tvDetailIcao = findViewById(R.id.tv_detail_icao);
        tvDetailCalc1 = findViewById(R.id.tv_detail_calc1);
        tvDetailCalc2 = findViewById(R.id.tv_detail_calc2);
        tvDetailCalc3 = findViewById(R.id.tv_detail_calc3);
        tvDetailCalc4 = findViewById(R.id.tv_detail_calc4);
        tvDetailCalc5 = findViewById(R.id.tv_detail_calc5);
        tvDetailKondisi = findViewById(R.id.tv_detail_kondisi);

        tvDetailNomor.setText(nomor);
        tvDetailHari.setText(hari);
        tvDetailTanggal.setText(tanggal);
        tvDetailIcao.setText(icao);
        tvDetailCalc1.setText(calc1);
        tvDetailCalc2.setText(calc2);
        tvDetailCalc3.setText(calc3);
        tvDetailCalc4.setText(calc4);
        tvDetailCalc5.setText(calc5);
        tvDetailKondisi.setText(kondisi);
    }
}