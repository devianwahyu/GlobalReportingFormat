package com.setiyawan.globalreportingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    // declare constant
    private static String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static String EXTRA_ROLE = "EXTRA_ROLE";
    private static String EXTRA_ICAO = "EXTRA_ICAO";

    // declare variables
    private EditText editEmail, editPassword;
    private TextView tvSwitchRegister;
    private AppCompatButton btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userRole, userEmail, userIcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // hide actionbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // init variables
        editEmail = findViewById(R.id.et_login_email);
        editPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSwitchRegister = findViewById(R.id.tv_switch_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // switch to register
        tvSwitchRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });

        // login button logic
        btnLogin.setOnClickListener(v -> {
            if (editEmail.length() > 0 && editPassword.length() > 0) {
                login(editEmail.getText().toString(), editPassword.getText().toString());
            } else {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // login logic
    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().getUser() != null) {
                            getUserRole(email);
                        } else {
                            Toast.makeText(getApplicationContext(), "Login gagal", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // check user's role
    private void getUserRole(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // check if email not found
                        if (task.isSuccessful()) {
                            QuerySnapshot doc = task.getResult();
                            // check if collection is null
                            if (doc.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Email belum terdaftar, silakan daftar dahulu", Toast.LENGTH_SHORT).show();
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> map = document.getData();
                                    userRole = (String) map.get("role");
                                    userEmail = (String) map.get("email");
                                    userIcao = (String) map.get("icao");
                                    if (userRole != null) {
                                        reload();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Gagal Login", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Email belum terdaftar, silakan daftar dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // navigator
    private void reload() {
        Intent intent;
        if (userRole.equalsIgnoreCase("inspektor")) {
            intent = new Intent(getApplicationContext(), InspectorActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), NavigatorActivity.class);
        }
        intent.putExtra(EXTRA_EMAIL, userEmail);
        intent.putExtra(EXTRA_ROLE, userRole);
        intent.putExtra(EXTRA_ICAO, userIcao);
        startActivity(intent);

        finish();
    }

    // check user logged in?
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getUserRole(currentUser.getEmail());
        }
    }
}