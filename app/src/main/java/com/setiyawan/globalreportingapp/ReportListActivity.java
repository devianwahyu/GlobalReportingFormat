package com.setiyawan.globalreportingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Objects;

public class ReportListActivity extends AppCompatActivity {
    // declare constant
    private static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static String EXTRA_ROLE = "EXTRA_ROLE";
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
    private RecyclerView rvReportList;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    private String userEmail, userRole, userIcao;
    private ReportModel reportModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        // hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // init variables
        rvReportList = findViewById(R.id.rv_report_list);
        db = FirebaseFirestore.getInstance();

        // get extras from intent
        Bundle bundle = getIntent().getExtras();
        userEmail = bundle.getString(EXTRA_EMAIL);
        userRole = bundle.getString(EXTRA_ROLE);
        userIcao = bundle.getString(EXTRA_ICAO);

        Query query;;

        if (userRole.equalsIgnoreCase("inspektor")) {
            query = db.collection("reports").whereEqualTo("petugas", userEmail).whereEqualTo("icao", userIcao);
        } else {
            query = db.collection("reports").whereEqualTo("icao", userIcao);
        }

        FirestoreRecyclerOptions<ReportModel> options = new FirestoreRecyclerOptions.Builder<ReportModel>()
                .setQuery(query, ReportModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ReportModel, ReportViewHolder>(options) {
            @NonNull
            @Override
            public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
                return new ReportViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReportViewHolder holder, int position, @NonNull ReportModel model) {
                holder.tvListPetugas.setText(model.getPetugas());
                holder.tvListWaktu.setText(String.format("%s, %s", model.getHari(), model.getTanggal()));
                holder.tvListIcao.setText(model.getIcao());
                holder.tvListNomor.setText(model.getNomor());

                holder.reportItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), ReportActivity.class);
                        intent.putExtra(EXTRA_ID, model.getId());
                        intent.putExtra(EXTRA_EMAIL, model.getPetugas());
                        intent.putExtra(EXTRA_HARI, model.getHari());
                        intent.putExtra(EXTRA_TANGGAL, model.getTanggal());
                        intent.putExtra(EXTRA_ICAO, model.getIcao());
                        intent.putExtra(EXTRA_NOMOR, model.getNomor());
                        intent.putExtra(EXTRA_CALC1, model.getCalc1());
                        intent.putExtra(EXTRA_CALC2, model.getCalc2());
                        intent.putExtra(EXTRA_CALC3, model.getCalc3());
                        intent.putExtra(EXTRA_CALC4, model.getCalc4());
                        intent.putExtra(EXTRA_CALC5, model.getCalc5());
                        intent.putExtra(EXTRA_KONDISI, model.getKondisi());
                        startActivity(intent);
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.collection("reports")
                                .whereEqualTo("id", model.getId())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        WriteBatch batch = FirebaseFirestore.getInstance().batch();

                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot snapshot: snapshotList) {
                                            batch.delete(snapshot.getReference());
                                        }

                                        batch.commit()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getApplicationContext(), "Report deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        };

        rvReportList.setHasFixedSize(true);
        rvReportList.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReportList.setAdapter(adapter);
    }

    private class ReportViewHolder extends RecyclerView.ViewHolder {

        // declare variables
        private TextView tvListPetugas;
        private TextView tvListWaktu;
        private TextView tvListIcao;
        private TextView tvListNomor;
        private ConstraintLayout reportItem;
        private AppCompatButton btnDelete;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            // init variables
            tvListPetugas = itemView.findViewById(R.id.tv_list_petugas);
            tvListWaktu = itemView.findViewById(R.id.tv_list_waktu);
            tvListIcao = itemView.findViewById(R.id.tv_list_icao);
            tvListNomor = itemView.findViewById(R.id.list_nomor);
            reportItem = itemView.findViewById(R.id.report_item);
            btnDelete = itemView.findViewById(R.id.btn_detail_delete);
        }
    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager{

        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}